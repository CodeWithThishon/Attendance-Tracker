package com.attendance.tracker.Repository;

import com.attendance.tracker.Entity.SchoolClass;
import com.attendance.tracker.Entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Boolean existsByRollNo(String rollNo);
    Optional<Student> findByRollNo(String rollNo);
    List<Student> findBySchoolClassId(Long classId);
    @Query("""
        SELECT s FROM Student s
        WHERE 
            (:search IS NULL OR 
             LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
             LOWER(s.rollNo) LIKE LOWER(CONCAT('%', :search, '%')))
        AND
            (:classId IS NULL OR s.schoolClass.id = :classId)
    """)
    Page<Student> searchStudents(
            String search,
            Long classId,
            Pageable pageable
    );
    List<Student> findBySchoolClass(SchoolClass schoolClass);
}
