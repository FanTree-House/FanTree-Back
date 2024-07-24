package com.example.fantreehouse.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WithdrawRequestDto {
    @NotBlank(message = "비밀번호 입력하세요")
    String password;
}
