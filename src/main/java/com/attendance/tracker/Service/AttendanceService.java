package com.attendance.tracker.Service;

import com.attendance.tracker.Dto.AttendanceDto;

import com.attendance.tracker.Entity.Student;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface AttendanceService {
    void markAttendance(AttendanceDto dto, Long teacherId);
    List<Student> viewAssignedStudents();
    double getMonthlyAttendancePercentage(Long studentId, int year, int month);

    double getTotalAttendancePercentage(Long studentId);
    List<Map<String, Object>> getTeacherStudentsPercentage(
            Authentication authentication,
            int year,
            int month
    );
}
