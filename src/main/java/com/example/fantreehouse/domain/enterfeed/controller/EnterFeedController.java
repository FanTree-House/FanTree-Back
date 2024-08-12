package com.example.fantreehouse.domain.enterfeed.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedRequestDto;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedResponseDto;
import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
import com.example.fantreehouse.domain.enterfeed.service.EnterFeedService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class EnterFeedController {

    private final EnterFeedService enterFeedService;
    @PostMapping("/{groupName}/notice")
    public ResponseEntity<ResponseMessageDto> createNotice(
            @PathVariable (value = "groupName") String groupName,
            @RequestBody EnterFeedRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        request.setCategory(FeedCategory.NOTICE);
        enterFeedService.createFeed(groupName, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.NOTICE_CREATE_SUCCESS));
    }

    @GetMapping("/{groupName}/notice/{feedId}")
    public ResponseEntity<ResponseDataDto<EnterFeedResponseDto>> getNotice(
            @PathVariable String groupName,
            @PathVariable Long feedId) {

        EnterFeedResponseDto notice = enterFeedService.getFeed(
            groupName, feedId, FeedCategory.NOTICE);
        return ResponseEntity.ok
            (new ResponseDataDto<>(ResponseStatus.NOTICE_RETRIEVE_SUCCESS, notice));
    }

    @GetMapping("/{groupName}/notice")
    public ResponseEntity<ResponseDataDto<List<EnterFeedResponseDto>>> getAllNotices(
        @PathVariable String groupName) {
        List<EnterFeedResponseDto> notices = enterFeedService
            .getAllFeeds(groupName, FeedCategory.NOTICE);
        return ResponseEntity.ok
            (new ResponseDataDto<>(ResponseStatus.NOTICE_RETRIEVE_SUCCESS, notices));
    }

    @PatchMapping("/{groupName}/notice/{feedId}")
    public ResponseEntity<ResponseMessageDto> updateNotice(
            @PathVariable String groupName,
            @PathVariable Long feedId,
            @RequestBody EnterFeedRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        request.setCategory(FeedCategory.NOTICE);
        enterFeedService.updateFeed(groupName, feedId, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.NOTICE_UPDATE_SUCCESS));
    }

    @DeleteMapping("/{groupName}/notice/{feedId}")
    public ResponseEntity<ResponseMessageDto> deleteNotice(
            @PathVariable String groupName,
            @PathVariable Long feedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        enterFeedService.deleteFeed(groupName, feedId, userDetails.getUser(), FeedCategory.NOTICE);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.NOTICE_DELETE_SUCCESS));
    }

    @PostMapping("/{groupName}/schedule")
    public ResponseEntity<ResponseMessageDto> createSchedule(
            @PathVariable String groupName,
            @RequestBody EnterFeedRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        request.setCategory(FeedCategory.SCHEDULE);
        enterFeedService.createFeed(groupName, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SCHEDULE_CREATE_SUCCESS));
    }

    @GetMapping("/{groupName}/schedule/{feedId}")
    public ResponseEntity<ResponseDataDto<EnterFeedResponseDto>> getSchedule(
            @PathVariable String groupName,
            @PathVariable Long feedId) {

        EnterFeedResponseDto schedule = enterFeedService.getFeed(
            groupName,
            feedId,
            FeedCategory.SCHEDULE);
        return ResponseEntity.ok
            (new ResponseDataDto<>(ResponseStatus.SCHEDULE_RETRIEVE_SUCCESS, schedule));
    }

    @GetMapping("/{groupName}/schedule")
    public ResponseEntity<ResponseDataDto<List<EnterFeedResponseDto>>> getAllSchedules(
        @PathVariable String groupName) {
        List<EnterFeedResponseDto> schedules = enterFeedService.
            getAllFeeds(groupName, FeedCategory.SCHEDULE);
        return ResponseEntity.ok
            (new ResponseDataDto<>(ResponseStatus.SCHEDULE_RETRIEVE_SUCCESS, schedules));
    }

    @PatchMapping("/{groupName}/schedule/{feedId}")
    public ResponseEntity<ResponseMessageDto> updateSchedule(
            @PathVariable String groupName,
            @PathVariable Long feedId,
            @RequestBody EnterFeedRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        request.setCategory(FeedCategory.SCHEDULE);
        enterFeedService.updateFeed(groupName, feedId, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SCHEDULE_UPDATE_SUCCESS));
    }

    @DeleteMapping("/{groupName}/schedule/{feedId}")
    public ResponseEntity<ResponseMessageDto> deleteSchedule(
            @PathVariable String groupName,
            @PathVariable Long feedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        enterFeedService.deleteFeed(groupName,feedId,userDetails.getUser(), FeedCategory.SCHEDULE);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SCHEDULE_DELETE_SUCCESS));
    }
}