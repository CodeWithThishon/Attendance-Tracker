package com.attendance.tracker.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SchoolClassDto {

    private Long id;

    @NotBlank(message = "Class name is required")
    private String className;

    private Long teacherId;
}
