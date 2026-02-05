package com.attendance.tracker.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentDto {

    private Long id;

    @NotBlank(message = "Student name is required")
    private String name;

    @NotBlank(message = "Roll number is required")
    private String rollNo;

    private boolean active = true;
@NotNull(message = "classId is Required")
    private Long classId;

    private String className;
}
