package com.placement;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.*;
import org.springframework.web.filter.OncePerRequestFilter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component class TokenService {
 private final byte[] key; private final long expiration;
 TokenService(@Value("${app.jwt-secret}") String secret,@Value("${app.jwt-expiration-ms}") long expiration){this.key=secret.getBytes(StandardCharsets.UTF_8);this.expiration=expiration;}
 String issue(UserEntity u){return Jwts.builder().subject(u.email).claim("role",u.role).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+expiration)).signWith(Keys.hmacShaKeyFor(key)).compact();}
 String subject(String token){return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(key)).build().parseSignedClaims(token).getPayload().getSubject();}
 String role(String token){return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(key)).build().parseSignedClaims(token).getPayload().get("role",String.class);}
}
@Component class JwtFilter extends OncePerRequestFilter {
 private final TokenService tokens; JwtFilter(TokenService tokens){this.tokens=tokens;}
 protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws java.io.IOException,ServletException{
  String h=req.getHeader("Authorization"); if(h!=null&&h.startsWith("Bearer "))try{String t=h.substring(7);String e=tokens.subject(t),r=tokens.role(t);var auth=new UsernamePasswordAuthenticationToken(e,null,List.of(new SimpleGrantedAuthority("ROLE_"+r)));org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);}catch(Exception ignored){org.springframework.security.core.context.SecurityContextHolder.clearContext();}
  chain.doFilter(req,res);
 }
}
@Configuration class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain filterChain(HttpSecurity http,JwtFilter jwt,@Value("${app.cors-origin}") String origin)throws Exception{
  return http.csrf(c->c.disable()).cors(c->c.configurationSource(req->{var x=new CorsConfiguration();x.setAllowedOrigins(List.of(origin));x.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));x.setAllowedHeaders(List.of("Authorization","Content-Type"));return x;})).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.requestMatchers("/api/auth/**","/api/public/**").permitAll().requestMatchers(org.springframework.http.HttpMethod.GET,"/api/jobs","/api/jobs/**").permitAll().requestMatchers("/api/admin/**").hasRole("ADMIN").requestMatchers("/api/profile/**","/api/resume/**","/api/applications/**","/api/dashboard/me","/api/settings/**","/api/notifications/**").hasRole("STUDENT").anyRequest().authenticated()).addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class).build();
 }
}
