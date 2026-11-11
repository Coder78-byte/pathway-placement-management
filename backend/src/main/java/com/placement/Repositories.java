package com.placement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
interface UserRepository extends JpaRepository<UserEntity,Long>{ Optional<UserEntity> findByEmail(String email); }
interface ProfileRepository extends JpaRepository<StudentProfile,Long>{ Optional<StudentProfile> findByUserId(Long id); }
interface JobRepository extends JpaRepository<Job,Long>{ List<Job> findByStatusOrderByPublishedAtDesc(String status); }
interface ApplicationRepository extends JpaRepository<ApplicationEntity,Long>{ boolean existsByStudentIdAndJobId(Long student,Long job); List<ApplicationEntity> findByStudentIdOrderByAppliedAtDesc(Long id); List<ApplicationEntity> findByJobIdOrderByAppliedAtDesc(Long id); long countByStatus(String status); }
interface SettingsRepository extends JpaRepository<UserSettings,Long>{}
interface NotificationRepository extends JpaRepository<NotificationEntity,Long>{ List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId); }
