package com.attendance.tracker.Dto;

import com.attendance.tracker.Entity.User.Role;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private Role role;
    public enum Role {
        ADMIN,
        TEACHER
    }
}
