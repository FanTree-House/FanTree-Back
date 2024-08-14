package com.example.fantreehouse.domain.communityfeed.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter

public class CommunityFeedRequestDto {

    @Size(min = 1, message = "작성할 내용을 입력해주세요")
    private final String contents;
    private final List<MultipartFile> file;

    @JsonCreator
    public CommunityFeedRequestDto(String contents, List<MultipartFile> file) {
        this.contents = contents;
        this.file = file;
    }
}
