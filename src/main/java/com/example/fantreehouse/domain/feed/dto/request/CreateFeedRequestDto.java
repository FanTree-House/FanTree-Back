package com.example.fantreehouse.domain.feed.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CreateFeedRequestDto {

    @NotBlank(message = "내용을 입력해 주세요.")
    @Size(max = 255, message = "입력가능한 글자수를 초과하였습니다.")
    String contents;
    private List<MultipartFile> file;

    @JsonCreator
    public CreateFeedRequestDto(String contents, List<MultipartFile> file) {
        this.contents = contents;
        this.file = file;
    }
}