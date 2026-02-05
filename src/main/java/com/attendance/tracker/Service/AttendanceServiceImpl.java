package com.attendance.tracker.Service;

import com.attendance.tracker.Dto.AttendanceDto;
import com.attendance.tracker.Dto.AttendanceRecordDto;
import com.attendance.tracker.Entity.Attendance;
import com.attendance.tracker.Entity.SchoolClass;
import com.attendance.tracker.Entity.Student;
import com.attendance.tracker.Entity.Teacher;
import com.attendance.tracker.Repository.AttendanceRepository;
import com.attendance.tracker.Repository.StudentRepository;
import com.attendance.tracker.Repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    final private AttendanceRepository attendanceRepository;
    final private StudentRepository studentRepository;
    final private TeacherRepository teacherRepository;
    @Override
    @Transactional
    public void markAttendance(AttendanceDto dto, Long teacherId) {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher Not Found"));

        if (teacher.getSchoolClass() == null) {
            throw new RuntimeException("Teacher has no assigned class");
        }

        for (AttendanceRecordDto record : dto.getRecords()) {

            Student student = studentRepository.findById(record.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student Not Found"));

            if (student.getSchoolClass() == null ||
                    !student.getSchoolClass().getId()
                            .equals(teacher.getSchoolClass().getId())) {

                throw new RuntimeException(
                        "Teacher not allowed to take attendance for this student"
                );
            }

            if (attendanceRepository.existsByStudentAndDate(student, dto.getDate())) {
                throw new RuntimeException(
                        "Attendance already marked for student: " + student.getName()
                );
            }

            Attendance attendance = Attendance.builder()
                    .student(student)
                    .markedBy(teacher)
                    .date(dto.getDate())
                    .status(record.getStatus())
                    .build();

            attendanceRepository.save(attendance);
        }
    }

    @Override
    public double getMonthlyAttendancePercentage(Long studentId, int year, int month) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        long totalDays = attendanceRepository
                .countByStudentAndDateBetween(student, startDate, endDate);

        if (totalDays == 0) {
            return 0.0;
        }

        long presentDays = attendanceRepository
                .countByStudentAndDateBetweenAndStatus(
                        student,
                        startDate,
                        endDate,
                        Attendance.Status.PRESENT
                );

        return (presentDays * 100.0) / totalDays;
    }

    @Override
    public double getTotalAttendancePercentage(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        long totalRecords = attendanceRepository.countByStudent(student);

        if (totalRecords == 0) {
            return 0.0;
        }

        long presentCount = attendanceRepository
                .countByStudentAndStatus(student, Attendance.Status.PRESENT);

        return (presentCount * 100.0) / totalRecords;
    }


    @Override
    public List<Map<String, Object>> getTeacherStudentsPercentage(
            Authentication authentication,
            int year,
            int month
    ) {
        if (month < 1 || month > 12) {
            throw new RuntimeException("Invalid month");
        }

        String email = authentication.getName();

        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        SchoolClass schoolClass = teacher.getSchoolClass();

        if (schoolClass == null) {
            return List.of(); // no assigned class
        }

        List<Student> students =
                studentRepository.findBySchoolClass(schoolClass);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Student student : students) {

            Map<String, Object> map = new HashMap<>();
            map.put("id", student.getId());
            map.put("name", student.getName());
            map.put("rollNo", student.getRollNo());

            map.put(
                    "monthlyPercentage",
                    getMonthlyAttendancePercentage(student.getId(), year, month)
            );

            map.put(
                    "totalPercentage",
                    getTotalAttendancePercentage(student.getId())
            );

            result.add(map);
        }

        return result;
    }

    @Override
        public List<Student> viewAssignedStudents() {

            String email = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();

            Teacher teacher = teacherRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            if (teacher.getSchoolClass() == null) {
                throw new RuntimeException("No class assigned to this teacher");
            }

            Long classId = teacher.getSchoolClass().getId();

            return studentRepository.findBySchoolClassId(classId);
        }
    }





