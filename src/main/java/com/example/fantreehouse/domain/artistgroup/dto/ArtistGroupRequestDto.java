package com.example.fantreehouse.domain.artistgroup.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ArtistGroupRequestDto {
    private String groupName;
    private String groupInfo;
    private List<Long> artistIds;
}