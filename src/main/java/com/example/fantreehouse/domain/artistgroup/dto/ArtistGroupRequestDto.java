package com.example.fantreehouse.domain.artistgroup.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ArtistGroupRequestDto {
    private String enterName;
    private String groupName;
    private String artistProfilePicture;
    private String groupInfo;
    private List<Long> artistIds;
}