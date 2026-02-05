package com.attendance.tracker.Service;

import com.attendance.tracker.Dto.StudentDto;
import com.attendance.tracker.Entity.Student;
import org.springframework.data.domain.Page;


import java.util.List;

public interface StudentService {
    Student createStudent(StudentDto dto);

    Student updateStudent(Long id, StudentDto dto);

    Student getStudentById(Long id);

    Page<Student> getAllStudents(
            String search,
            Long classId,
            int page,
            int size
    );

    void deleteStudent(Long id);

    List<Student> getStudentsByClass(Long classId);
}

