package com.attendance.tracker.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentAttendancePercentageDto {

    private Long studentId;
    private String name;
    private String rollNo;
    private double monthlyPercentage;
    private double totalPercentage;
}
