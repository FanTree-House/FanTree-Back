package com.example.fantreehouse.domain.artistgroup.dto;

import com.example.fantreehouse.domain.artistgroup.deserializer.StringToLongListDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class ArtistGroupRequestDto {
    private String enterName;
    private String groupName;
    private String groupInfo;
    @JsonDeserialize(using = StringToLongListDeserializer.class)
    private List<Long> artistIds;
    @NotNull(message = "그룹 프로필 사진을 업로드해주세요.")
    private MultipartFile file;

    @JsonCreator
    public ArtistGroupRequestDto(String enterName, String groupName, String groupInfo, List<Long> artistIds, MultipartFile file) {
        this.enterName = enterName;
        this.groupName = groupName;
        this.groupInfo = groupInfo;
        this.artistIds = artistIds;
        this.file = file;
    }
}