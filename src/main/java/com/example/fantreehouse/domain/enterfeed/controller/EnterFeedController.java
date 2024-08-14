package com.example.fantreehouse.domain.enterfeed.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedRequestDto;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedResponseDto;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedResponseDtoExtension;
import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
import com.example.fantreehouse.domain.enterfeed.service.EnterFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class EnterFeedController {

    private final EnterFeedService enterFeedService;

    /**
     * 공지사항 생성
     * @param groupName 그룹 이름
     * @param request 공지사항 요청 DTO
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @PostMapping("/{groupName}/notice")
    public ResponseEntity<ResponseMessageDto> createNotice(
            @PathVariable (value = "groupName") String groupName,
            @RequestBody EnterFeedRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        request.setCategory(FeedCategory.NOTICE);
        enterFeedService.createFeed(groupName, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.NOTICE_CREATE_SUCCESS));
    }

    /**
     * 특정 공지사항 조회
     * @param groupName 그룹 이름
     * @param feedId 공지사항 ID
     * @return 공지사항 응답 DTO
     */
    @GetMapping("/{groupName}/notice/{feedId}")
    public ResponseEntity<ResponseDataDto<EnterFeedResponseDto>> getNotice(
            @PathVariable String groupName,
            @PathVariable Long feedId) {

        EnterFeedResponseDto notice = enterFeedService.getFeed(
            groupName, feedId, FeedCategory.NOTICE);
        return ResponseEntity.ok
            (new ResponseDataDto<>(ResponseStatus.NOTICE_RETRIEVE_SUCCESS, notice));
    }

    /**
     * 모든 공지사항 조회
     * @param groupName 그룹 이름
     * @return 공지사항 응답 DTO 목록
     */
    @GetMapping("/{groupName}/notice")
    public ResponseEntity<ResponseDataDto<List<EnterFeedResponseDto>>> getAllNotices(
        @PathVariable String groupName) {
        List<EnterFeedResponseDto> notices = enterFeedService
            .getAllFeeds(groupName, FeedCategory.NOTICE);
        return ResponseEntity.ok
            (new ResponseDataDto<>(ResponseStatus.NOTICE_RETRIEVE_SUCCESS, notices));
    }

    /**
     * 특정 그룹 소속사의 모든 공지사항 조회 (createdAt 포함)
     * @param groupName
     * @return
     */
    @GetMapping("/{groupName}/notices/createdAt")
    public ResponseEntity<ResponseDataDto<List<EnterFeedResponseDtoExtension>>> getSortedAllNotices(
            @PathVariable String groupName) {

        List<EnterFeedResponseDtoExtension> sortedNotices = enterFeedService
                .getSortedAllNotices(groupName);
        return ResponseEntity.ok
                (new ResponseDataDto<>(ResponseStatus.NOTICE_RETRIEVE_SUCCESS, sortedNotices));
    }

    /**
     * 공지사항 수정
     * @param groupName 그룹 이름
     * @param feedId 공지사항 ID
     * @param request 공지사항 요청 DTO
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
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

    /**
     * 공지사항 삭제
     * @param groupName 그룹 이름
     * @param feedId 공지사항 ID
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @DeleteMapping("/{groupName}/notice/{feedId}")
    public ResponseEntity<ResponseMessageDto> deleteNotice(
            @PathVariable String groupName,
            @PathVariable Long feedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        enterFeedService.deleteFeed(groupName, feedId, userDetails.getUser(), FeedCategory.NOTICE);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.NOTICE_DELETE_SUCCESS));
    }

    /**
     * 일정 생성
     * @param groupName 그룹 이름
     * @param request 일정 요청 DTO
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @PostMapping("/{groupName}/schedule")
    public ResponseEntity<ResponseMessageDto> createSchedule(
            @PathVariable String groupName,
            @RequestBody EnterFeedRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        request.setCategory(FeedCategory.SCHEDULE);
        enterFeedService.createFeed(groupName, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SCHEDULE_CREATE_SUCCESS));
    }

    /**
     * 특정 일정 조회
     * @param groupName 그룹 이름
     * @param feedId 일정 ID
     * @return 일정 응답 DTO
     */
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

    /**
     * 모든 일정 조회
     * @param groupName 그룹 이름
     * @return 일정 응답 DTO 목록
     */
    @GetMapping("/{groupName}/schedule")
    public ResponseEntity<ResponseDataDto<List<EnterFeedResponseDto>>> getAllSchedules(
        @PathVariable String groupName) {
        List<EnterFeedResponseDto> schedules = enterFeedService.
            getAllFeeds(groupName, FeedCategory.SCHEDULE);
        return ResponseEntity.ok
            (new ResponseDataDto<>(ResponseStatus.SCHEDULE_RETRIEVE_SUCCESS, schedules));
    }

    /**
     * 특정 그룹 소속사의 모든 일정 조회 (createdAt 포함)
     * @param groupName
     * @return
     */
    @GetMapping("/{groupName}/schedule/createdAt")
    public ResponseEntity<ResponseDataDto<List<EnterFeedResponseDtoExtension>>> getSortedAllSchedules(
            @PathVariable String groupName) {
        List<EnterFeedResponseDtoExtension> sortedSchedules = enterFeedService
                .getSortedAllSchedules(groupName);
        return ResponseEntity.ok
                (new ResponseDataDto<>(ResponseStatus.NOTICE_RETRIEVE_SUCCESS, sortedSchedules));
    }

    /**
     * 일정 수정
     * @param groupName 그룹 이름
     * @param feedId 일정 ID
     * @param request 일정 요청 DTO
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
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

    /**
     * 일정 삭제
     * @param groupName 그룹 이름
     * @param feedId 일정 ID
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @DeleteMapping("/{groupName}/schedule/{feedId}")
    public ResponseEntity<ResponseMessageDto> deleteSchedule(
            @PathVariable String groupName,
            @PathVariable Long feedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        enterFeedService.deleteFeed(groupName,feedId,userDetails.getUser(), FeedCategory.SCHEDULE);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SCHEDULE_DELETE_SUCCESS));
    }
}