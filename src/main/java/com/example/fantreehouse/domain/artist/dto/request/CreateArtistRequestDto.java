package com.example.fantreehouse.domain.artist.dto.request;

import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateArtistRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "활동명은 영어와 숫자만 입력 가능합니다.")
    @Size(min = 1, max = 20, message = "활동명은 최대 20글자 입니다.")
    @NotBlank(message = "활동명을 입력해주세요.")
    private String artistName; //활동명

    @NotBlank(message = "프로필사진을 업로드해주세요.")
    private String artistProfilePicture;

}
