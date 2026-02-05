package com.attendance.tracker.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AttendanceDto {

    @NotNull
    private LocalDate date;

    @NotEmpty
    private List<AttendanceRecordDto> records;
}

