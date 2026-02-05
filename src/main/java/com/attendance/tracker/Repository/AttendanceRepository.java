package com.attendance.tracker.Repository;

import com.attendance.tracker.Entity.Attendance;
import com.attendance.tracker.Entity.Student;
import com.attendance.tracker.Entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    boolean existsByStudentAndDate(Student student, LocalDate date);
    long countByStudentAndDateBetween(Student student, LocalDate start, LocalDate end);

    long countByStudentAndDateBetweenAndStatus(
            Student student,
            LocalDate start,
            LocalDate end,
            Attendance.Status status);

    long countByStudent(Student student);

    long countByStudentAndStatus(Student student, Attendance.Status status);
    void deleteByStudent(Student student);
    void deleteByMarkedBy(Teacher teacher);
    @Query("""
        SELECT a.student, 
               COUNT(a), 
               SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END)
        FROM Attendance a
        WHERE a.date BETWEEN :start AND :end
        GROUP BY a.student
    """)
    List<Object[]> getMonthlyStats(LocalDate start, LocalDate end);

    /* 🔹 Get total attendance (bulk) */
    @Query("""
        SELECT a.student,
               COUNT(a),
               SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END)
        FROM Attendance a
        GROUP BY a.student
    """)
    List<Object[]> getTotalStats();

}
