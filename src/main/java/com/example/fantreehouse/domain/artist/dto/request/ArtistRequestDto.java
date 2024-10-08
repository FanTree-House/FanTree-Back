package com.example.fantreehouse.domain.artist.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ArtistRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "활동명은 영어와 숫자만 입력 가능합니다.")
    @Size(min = 1, max = 20, message = "활동명은 최대 20글자 입니다.")
    @NotBlank(message = "활동명을 입력해주세요.")
    private String artistName; //활동명

    @Size(min = 1, max = 100, message = "한 줄 소개는 최대 20글자 입니다.")
    private String introduction;

    @NotNull(message = "프로필사진을 업로드해주세요.") //x-www-form 에서 인식 못함
    private MultipartFile file;

//  또한 form-data 에서만 인식가능한데 그럼 x-www-form 사용 못함
//    따라서 Dto도 form-data에서 받아야 함

    @JsonCreator //object mapper에서 생성자를 찾는 어노테이션 생성자를 못찾을떄 , 비선호어노테이션
    public ArtistRequestDto(String artistName,String introduction, MultipartFile file) {
        this.artistName = artistName;
        this.introduction = introduction;
        this.file = file;
    }
}
