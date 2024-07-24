package com.example.fantreehouse.domain.artistgroup.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.domain.artistgroup.dto.ArtistGroupRequestDto;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.service.ArtistGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entertainments")
public class ArtistGroupController {

    private final ArtistGroupService artistGroupService;

    @Autowired
    public ArtistGroupController(ArtistGroupService artistGroupService) {
        this.artistGroupService = artistGroupService;
    }

    @PostMapping("/{entername}")
    public ResponseEntity<ResponseMessageDto> createArtistGroup(@PathVariable String entername, @RequestBody ArtistGroupRequestDto request) {
        artistGroupService.createArtistGroup(entername, request);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_GROUP_CREATE_SUCCESS));
    }

    @GetMapping("/{entername}/{groupName}")
    public ResponseEntity<ResponseDataDto<ArtistGroup>> getArtistGroup(@PathVariable String entername, @PathVariable String groupName) {
        ArtistGroup artistGroup = artistGroupService.getArtistGroup(entername, groupName);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ARTIST_GROUP_RETRIEVE_SUCCESS, artistGroup));
    }

    @GetMapping("/{entername}/group_name")
    public ResponseEntity<ResponseDataDto<List<ArtistGroup>>> getAllArtistGroups(@PathVariable String entername) {
        List<ArtistGroup> artistGroups = artistGroupService.getAllArtistGroups(entername);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ARTIST_GROUP_RETRIEVE_SUCCESS, artistGroups));
    }

    @PatchMapping("/{entername}/{groupName}")
    public ResponseEntity<ResponseMessageDto> updateArtistGroup(@PathVariable String entername, @PathVariable String groupName, @RequestBody ArtistGroupRequestDto request) {
        artistGroupService.updateArtistGroup(entername, groupName, request);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_GROUP_UPDATE_SUCCESS));
    }

    @DeleteMapping("/{entername}/{groupName}")
    public ResponseEntity<ResponseMessageDto> deleteArtistGroup(@PathVariable String entername, @PathVariable String groupName) {
        artistGroupService.deleteArtistGroup(entername, groupName);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_GROUP_DELETE_SUCCESS));
    }
}