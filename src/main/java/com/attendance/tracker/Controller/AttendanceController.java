package com.attendance.tracker.Controller;

import com.attendance.tracker.Dto.AttendanceDto;
import com.attendance.tracker.Dto.StudentAttendancePercentageDto;
import com.attendance.tracker.Entity.Attendance;
import com.attendance.tracker.Entity.Student;
import com.attendance.tracker.Service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    final private AttendanceService attendanceService;


    @PostMapping("/teacher/{teacherId}")
    public ResponseEntity<String> markAttendance(
            @PathVariable Long teacherId,
            @Valid @RequestBody AttendanceDto dto) {

        attendanceService.markAttendance(dto, teacherId);
        return ResponseEntity.ok("Attendance marked successfully");
    }


    @GetMapping("/teacher/percentage")
    public ResponseEntity<List<Map<String, Object>>> getTeacherStudentsPercentage(
            Authentication authentication,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(
                attendanceService.getTeacherStudentsPercentage(
                        authentication, year, month
                )
        );
    }



    @GetMapping("/students")
    public ResponseEntity<List<Student>> viewAssignedStudents() {
        return ResponseEntity.ok(attendanceService.viewAssignedStudents());
    }
}
