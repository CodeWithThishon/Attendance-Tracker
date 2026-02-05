package com.attendance.tracker.Controller;

import com.attendance.tracker.Dto.TeacherDto;
import com.attendance.tracker.Entity.Teacher;

import com.attendance.tracker.Service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/teachers")
@RequiredArgsConstructor
public class TeacherController {

   final private TeacherService teacherService;
    @PostMapping
    public ResponseEntity<Teacher> createTeacher(
            @Valid @RequestBody TeacherDto dto
    ) {
        Teacher created = teacherService.createTeacher(dto);
        return ResponseEntity.ok(created);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeacher(@PathVariable Long id){
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok("Teacher Deleted Successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(
            @PathVariable("id") Long id,
            @Valid @RequestBody TeacherDto dto) {
        Teacher updatedTeacher = teacherService.updateTeacher(id, dto);
        return ResponseEntity.ok(updatedTeacher);
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers()   {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherByID(@PathVariable Long id){
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }
}
