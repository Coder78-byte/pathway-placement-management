CREATE TABLE users (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, full_name VARCHAR(120) NOT NULL, email VARCHAR(190) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL, role VARCHAR(20) NOT NULL, enabled BOOLEAN NOT NULL DEFAULT TRUE,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE student_profiles (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT NOT NULL UNIQUE, phone VARCHAR(30), date_of_birth DATE,
 gender VARCHAR(40), address VARCHAR(255), city VARCHAR(100), state VARCHAR(100), college VARCHAR(180), department VARCHAR(120),
 degree VARCHAR(120), specialization VARCHAR(120), semester INT, graduation_year INT, cgpa DECIMAL(4,2), backlogs INT DEFAULT 0,
 skills TEXT, programming_languages TEXT, certifications TEXT, projects TEXT, internships TEXT, work_experience TEXT,
 linkedin_url VARCHAR(255), github_url VARCHAR(255), portfolio_url VARCHAR(255), resume_path VARCHAR(500), resume_filename VARCHAR(255), resume_uploaded_at TIMESTAMP NULL,
 CONSTRAINT fk_profile_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE jobs (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, company_name VARCHAR(180) NOT NULL, title VARCHAR(180) NOT NULL, location VARCHAR(180), employment_type VARCHAR(60),
 salary VARCHAR(120), description TEXT, responsibilities TEXT, qualifications TEXT, skills TEXT, min_cgpa DECIMAL(4,2), departments VARCHAR(500),
 deadline DATE, selection_process TEXT, additional_requirements TEXT, status VARCHAR(20) NOT NULL DEFAULT 'DRAFT', published_at TIMESTAMP NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE applications (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, student_id BIGINT NOT NULL, job_id BIGINT NOT NULL, status VARCHAR(32) NOT NULL DEFAULT 'APPLIED',
 applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 CONSTRAINT uq_application_student_job UNIQUE(student_id, job_id), CONSTRAINT fk_application_student FOREIGN KEY(student_id) REFERENCES users(id),
 CONSTRAINT fk_application_job FOREIGN KEY(job_id) REFERENCES jobs(id)
);
CREATE TABLE user_settings (user_id BIGINT PRIMARY KEY, email_notifications BOOLEAN NOT NULL DEFAULT TRUE, application_updates BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT fk_settings_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE);
CREATE TABLE notifications (id BIGINT PRIMARY KEY AUTO_INCREMENT, user_id BIGINT NOT NULL, title VARCHAR(180) NOT NULL, message VARCHAR(500) NOT NULL,
 read_at TIMESTAMP NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, CONSTRAINT fk_notification_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE);
CREATE INDEX idx_jobs_status_deadline ON jobs(status, deadline);
CREATE INDEX idx_applications_student ON applications(student_id, applied_at);
