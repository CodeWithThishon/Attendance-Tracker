package com.attendance.tracker.Service;

import com.attendance.tracker.Dto.SchoolClassDto;
import com.attendance.tracker.Entity.SchoolClass;
import com.attendance.tracker.Entity.Teacher;

import java.util.List;

public interface SchoolClassService {

    SchoolClass createSchoolClass(SchoolClassDto dto);

    void deleteSchoolClass(Long id);

    List<SchoolClass> getAllClasses();

    SchoolClass getClassById(Long id);

    Teacher getTeacherByClassId(Long classId);
}
