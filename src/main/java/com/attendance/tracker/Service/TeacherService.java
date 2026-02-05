package com.attendance.tracker.Service;

import com.attendance.tracker.Dto.TeacherDto;

import com.attendance.tracker.Entity.Teacher;

import java.util.List;


public interface TeacherService {
    Teacher createTeacher(TeacherDto dto);

    List<Teacher> getAllTeachers();
//
//    Teacher getTeacherById(Long id);

    Teacher updateTeacher(Long id, TeacherDto dto);

    void deleteTeacher(Long id);

    Teacher getTeacherById(Long id);

}
