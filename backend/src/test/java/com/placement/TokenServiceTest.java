package com.placement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {
    @Test
    void issuedTokenCarriesIdentityAndRoleAndCanBeParsed() {
        TokenService service = new TokenService(
                "test-only-secret-that-is-long-enough-for-hmac-sha256-1234567890",
                60_000L);
        UserEntity user = new UserEntity();
        user.email = "student@example.edu";
        user.role = "STUDENT";

        String token = service.issue(user);

        assertEquals(user.email, service.subject(token));
        assertEquals(user.role, service.role(token));
        assertThrows(Exception.class, () -> service.subject(token + "invalid"));
    }
}
