package com.example.fantreehouse.domain.entertainment.dto;

import com.example.fantreehouse.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
public class EntertainmentRequestDto {
    @NotBlank(message = "소속사 이름을 입력해주세요.")
    @NotNull
    @Size(min = 1, max = 20, message = "소속사 이름은 최대 20글자 입니다.")
//    @UniqueElements(message = "이미 존재하는 소속사 이름입니다.")
    private String enterName;

    @NotNull(message = "사업자 번호를 입력해주세요.")
    private Long enterNumber;

    @NotBlank(message = "소속사 로고를 업로드해주세요.")
    private String enterLogo;

}