package com.attendance.tracker.Service;

import com.attendance.tracker.Dto.SchoolClassDto;
import com.attendance.tracker.Entity.SchoolClass;
import com.attendance.tracker.Entity.Teacher;
import com.attendance.tracker.Repository.SchoolClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolClassServiceImpl implements SchoolClassService {

    final private SchoolClassRepository schoolClassRepository;

    @Override
    public SchoolClass createSchoolClass(SchoolClassDto dto) {
        if (schoolClassRepository.existsByClassName(dto.getClassName())){
            throw new RuntimeException("Class With This Name is Already Exist");
        }
        SchoolClass schoolClass=SchoolClass.builder().className(dto.getClassName()).build();
        return schoolClassRepository.save(schoolClass);
    }

    @Override
    public void deleteSchoolClass(Long id){
        SchoolClass schoolClass=schoolClassRepository.findById(id).orElseThrow(()->new RuntimeException("No Class Found"));
        Teacher teacher = schoolClass.getTeacher();
        if (teacher != null) {
            teacher.setSchoolClass(null);
        }
        schoolClassRepository.delete(schoolClass);
    }

    public List<SchoolClass> getAllClasses(){
        return schoolClassRepository.findAll();
    }

    public SchoolClass getClassById(Long id){
       return schoolClassRepository.findById(id).orElseThrow(()->new RuntimeException("Class Not Found"));

    }

    @Override
    public Teacher getTeacherByClassId(Long classId) {

        SchoolClass schoolClass = schoolClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        if (schoolClass.getTeacher() == null) {
            throw new RuntimeException("No teacher assigned to this class");
        }

        return schoolClass.getTeacher();
    }

}
