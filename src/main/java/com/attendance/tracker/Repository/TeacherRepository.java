package com.attendance.tracker.Repository;

import com.attendance.tracker.Entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    Optional<Teacher> findByEmail(String email);

    boolean existsByEmail(String email);



}