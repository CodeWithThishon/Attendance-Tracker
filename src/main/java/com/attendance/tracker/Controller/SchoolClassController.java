package com.attendance.tracker.Controller;

import com.attendance.tracker.Dto.SchoolClassDto;
import com.attendance.tracker.Entity.SchoolClass;
import com.attendance.tracker.Entity.Teacher;
import com.attendance.tracker.Service.SchoolClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/class")
@RequiredArgsConstructor
public class SchoolClassController {
    final private SchoolClassService schoolClassService;

    @PostMapping
    public ResponseEntity<SchoolClass> createSchoolClass(@Valid @RequestBody SchoolClassDto dto){
        SchoolClass created= schoolClassService.createSchoolClass(dto);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchoolClass(@PathVariable Long id){
      schoolClassService.deleteSchoolClass(id);
      return ResponseEntity.ok("Class Deleted Successfully");
    }

    @GetMapping
    public ResponseEntity<List<SchoolClass>> getAllClasses(){
        List<SchoolClass> showAll= schoolClassService.getAllClasses();
        return ResponseEntity.ok(showAll);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolClass> getClassById(@PathVariable Long id){
        return ResponseEntity.ok(schoolClassService.getClassById(id));
    }

    @GetMapping("/teacher/{classId}")
    public Teacher getTeacherByClass(@PathVariable Long classId) {
        return schoolClassService.getTeacherByClassId(classId);
    }

}
