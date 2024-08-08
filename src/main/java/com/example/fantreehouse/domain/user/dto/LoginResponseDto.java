package com.example.fantreehouse.domain.user.dto;

import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String userId;
    private UserRoleEnum userRole;

    public LoginResponseDto(String userId, UserRoleEnum userRole) {
        this.userId = userId;
        this.userRole = userRole;
    }
}