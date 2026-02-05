package com.attendance.tracker.Service;

import com.attendance.tracker.Dto.StudentDto;
import com.attendance.tracker.Entity.SchoolClass;
import com.attendance.tracker.Entity.Student;
import com.attendance.tracker.Repository.AttendanceRepository;
import com.attendance.tracker.Repository.SchoolClassRepository;
import com.attendance.tracker.Repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
    final private StudentRepository studentRepository;
    final private SchoolClassRepository schoolClassRepository;
    final private AttendanceRepository attendanceRepository;
    @Override
    public Student createStudent(StudentDto dto){
        if (studentRepository.existsByRollNo(dto.getRollNo())) {
            throw new RuntimeException("Student With This RollNo Already Exist");
        }
        SchoolClass schoolClass=schoolClassRepository.findById(dto.getClassId()).orElseThrow(()->new RuntimeException("Class Not Found"));
            Student student=Student.builder().name(dto.getName()).rollNo(dto.getRollNo()).schoolClass(schoolClass).build();
            return studentRepository.save(student);
    }

    @Override
    public     Student updateStudent(Long id, StudentDto dto){
        Student student=studentRepository.findById(id).orElseThrow(()->new RuntimeException("Student Not Found"));
        if (!student.getRollNo().equals(dto.getRollNo()) && studentRepository.existsByRollNo(dto.getRollNo())){
            throw new RuntimeException("Roll No Already Exist");
        }
        SchoolClass schoolClass=schoolClassRepository.findById(dto.getClassId()).orElseThrow(()->new RuntimeException("Class Not Found"));
        student.setName(dto.getName());
        student.setRollNo(dto.getRollNo());
        student.setSchoolClass(schoolClass);
       return studentRepository.save(student);
    }

    @Override
    public  Student getStudentById(Long id){
        return studentRepository.findById(id).orElseThrow(()->new RuntimeException("Student Not Found"));
    }

    @Override
    public Page<Student> getAllStudents(
            String search,
            Long classId,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return studentRepository.searchStudents(
                (search == null || search.isBlank()) ? null : search,
                classId,
                pageable
        );
    }


    @Override
    @Transactional
    public  void deleteStudent(Long id){
        Student student=studentRepository.findById(id).orElseThrow(()->new RuntimeException("Student Not Found"));
        attendanceRepository.deleteByStudent(student);
        studentRepository.delete(student);
    }

    @Override
    public List<Student> getStudentsByClass(Long classId) {
        return studentRepository.findBySchoolClassId(classId);
    }

}
