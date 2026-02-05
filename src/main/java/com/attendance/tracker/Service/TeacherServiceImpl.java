package com.attendance.tracker.Service;

import com.attendance.tracker.Dto.TeacherDto;
import com.attendance.tracker.Entity.SchoolClass;
import com.attendance.tracker.Entity.Teacher;
import com.attendance.tracker.Entity.User;
import com.attendance.tracker.Repository.AttendanceRepository;
import com.attendance.tracker.Repository.SchoolClassRepository;
import com.attendance.tracker.Repository.TeacherRepository;
import com.attendance.tracker.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService{
    final private TeacherRepository teacherRepository;
    final private SchoolClassRepository schoolClassRepository;
    final private AttendanceRepository attendanceRepository;
 final private PasswordEncoder passwordEncoder;
 final private UserRepository userRepository;
    @Override
    public Teacher createTeacher(TeacherDto dto) {

                if (teacherRepository.existsByEmail(dto.getEmail())) {
                    throw new RuntimeException("Teacher email already exists");
                }

                if (userRepository.existsByEmail(dto.getEmail())) {
                    throw new RuntimeException("Login already exists for this email");
                }

                SchoolClass schoolClass = schoolClassRepository.findById(dto.getClassId())
                        .orElseThrow(() -> new RuntimeException("Class not found"));

                Teacher teacher = Teacher.builder()
                        .name(dto.getName())
                        .email(dto.getEmail())
                        .schoolClass(schoolClass)
                        .build();

                teacherRepository.save(teacher);

                User user = User.builder()
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .role(User.Role.TEACHER)
                        .teacher(teacher)
                        .build();

                userRepository.save(user);

                return teacher;
            }

        @Override
    public Teacher updateTeacher(Long teacherId, TeacherDto dto) {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // 🔹 ADD THIS
        User user = userRepository.findByTeacher(teacher)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!teacher.getEmail().equals(dto.getEmail()) &&
                teacherRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Another teacher with this email already exists");
        }

        teacher.setName(dto.getName());
        teacher.setEmail(dto.getEmail());

        // 🔹 ADD THIS (sync login email)
        user.setEmail(dto.getEmail());

        // 🔹 ADD THIS (update password only if provided)
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getClassId() != null) {

            SchoolClass newClass = schoolClassRepository.findById(dto.getClassId())
                    .orElseThrow(() -> new RuntimeException("Class not found"));

            if (newClass.getTeacher() != null &&
                    !newClass.getTeacher().getId().equals(teacherId)) {
                throw new RuntimeException("Class already assigned to another teacher");
            }

            if (teacher.getSchoolClass() != null) {
                SchoolClass oldClass = teacher.getSchoolClass();
                oldClass.setTeacher(null);
                schoolClassRepository.save(oldClass);
            }

            newClass.setTeacher(teacher);
            teacher.setSchoolClass(newClass);
            schoolClassRepository.save(newClass);

        } else {
            if (teacher.getSchoolClass() != null) {
                SchoolClass oldClass = teacher.getSchoolClass();
                oldClass.setTeacher(null);
                schoolClassRepository.save(oldClass);
                teacher.setSchoolClass(null);
            }
        }

        // 🔹 ADD THIS
        userRepository.save(user);

        return teacherRepository.save(teacher);
    }


    @Override
    @Transactional
    public  void deleteTeacher(Long id){
        Teacher teacher=teacherRepository.findById(id).orElseThrow(() -> new RuntimeException("Teacher not found"));
        if(teacher.getSchoolClass() !=null) {
            teacher.getSchoolClass().setTeacher(null);
        }
        attendanceRepository.deleteByMarkedBy(teacher);
            teacherRepository.delete(teacher);
        }
@Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacherById(Long id){
        return teacherRepository.findById(id).orElseThrow(()-> new RuntimeException("Teacher Not Found"));
    }

}
