package com.placement;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;

@Entity @Table(name="users") class UserEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(name="full_name",nullable=false) public String fullName;
 @Column(nullable=false,unique=true) public String email;
 @Column(name="password_hash",nullable=false) public String passwordHash;
 @Column(nullable=false) public String role="STUDENT";
 public boolean enabled=true;
 @Column(name="created_at",insertable=false,updatable=false) public LocalDateTime createdAt;
}
@Entity @Table(name="student_profiles") class StudentProfile {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @OneToOne @JoinColumn(name="user_id",nullable=false,unique=true) public UserEntity user;
 public String phone; @Column(name="date_of_birth") public LocalDate dateOfBirth; public String gender,address,city,state,college,department,degree,specialization;
 public Integer semester; @Column(name="graduation_year") public Integer graduationYear; public BigDecimal cgpa; public Integer backlogs;
 @Column(columnDefinition="text") public String skills,programmingLanguages,certifications,projects,internships,workExperience;
 @Column(name="linkedin_url") public String linkedinUrl; @Column(name="github_url") public String githubUrl; @Column(name="portfolio_url") public String portfolioUrl;
 @Column(name="resume_path") public String resumePath; @Column(name="resume_filename") public String resumeFilename; @Column(name="resume_uploaded_at") public LocalDateTime resumeUploadedAt;
}
@Entity @Table(name="jobs") class Job {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(name="company_name",nullable=false) public String companyName; @Column(nullable=false) public String title; public String location;
 @Column(name="employment_type") public String employmentType; public String salary;
 @Column(columnDefinition="text") public String description,responsibilities,qualifications,skills;
 @Column(name="min_cgpa") public BigDecimal minCgpa; public String departments; public LocalDate deadline;
 @Column(name="selection_process",columnDefinition="text") public String selectionProcess;
 @Column(name="additional_requirements",columnDefinition="text") public String additionalRequirements;
 @Column(nullable=false) public String status="DRAFT"; @Column(name="published_at") public LocalDateTime publishedAt;
 @Column(name="created_at",insertable=false,updatable=false) public LocalDateTime createdAt;
}
@Entity @Table(name="applications",uniqueConstraints=@UniqueConstraint(columnNames={"student_id","job_id"})) class ApplicationEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne @JoinColumn(name="student_id",nullable=false) public UserEntity student;
 @ManyToOne @JoinColumn(name="job_id",nullable=false) public Job job;
 @Column(nullable=false) public String status="APPLIED";
 @Column(name="applied_at",insertable=false,updatable=false) public LocalDateTime appliedAt;
}
@Entity @Table(name="user_settings") class UserSettings {
 @Id @Column(name="user_id") public Long userId;
 @Column(name="email_notifications") public boolean emailNotifications=true;
 @Column(name="application_updates") public boolean applicationUpdates=true;
}
@Entity @Table(name="notifications") class NotificationEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(name="user_id",nullable=false) public Long userId;
 @Column(nullable=false) public String title;
 @Column(nullable=false) public String message;
 @Column(name="read_at") public LocalDateTime readAt;
 @Column(name="created_at",insertable=false,updatable=false) public LocalDateTime createdAt;
}
