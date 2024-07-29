package com.example.fantreehouse.domain.user.dto;

import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdminRequestDto {

    @NotBlank(message = "수정할 유저의 아이디를 입력하세요.")
    private String loginId;

    private UserRoleEnum userRole;
}
