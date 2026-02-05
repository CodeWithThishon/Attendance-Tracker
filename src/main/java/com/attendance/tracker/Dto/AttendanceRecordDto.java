package com.attendance.tracker.Dto;

import com.attendance.tracker.Entity.Attendance;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendanceRecordDto {

    @NotNull
    private Long studentId;

    @NotNull
    private Attendance.Status status;
}

