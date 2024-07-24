package com.example.fantreehouse.domain.enterfeed.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedRequestDto;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedResponseDto;
import com.example.fantreehouse.domain.enterfeed.service.EnterFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feed")
public class EnterFeedController {

    private final EnterFeedService enterFeedService;

    @Autowired
    public EnterFeedController(EnterFeedService enterFeedService) {
        this.enterFeedService = enterFeedService;
    }

    /**
     * [createNotice] 공지사항 생성
     * @param groupName 그룹 이름
     * @param request 요청 객체
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @PostMapping("/{groupName}/notice")
    public ResponseEntity<ResponseMessageDto> createNotice(
            @PathVariable String groupName,
            @RequestBody EnterFeedRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        enterFeedService.createNotice(groupName, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.NOTICE_CREATE_SUCCESS));
    }

    /**
     * [getNotice] 특정 공지사항 조회
     * @param groupName 그룹 이름
     * @param feedId 피드 ID
     * @return 공지사항 응답 DTO
     */
    @GetMapping("/{groupName}/notice/{feedId}")
    public ResponseEntity<ResponseDataDto<EnterFeedResponseDto>> getNotice(
            @PathVariable String groupName,
            @PathVariable String feedId) {

        EnterFeedResponseDto notice = enterFeedService.getNotice(groupName, feedId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.NOTICE_RETRIEVE_SUCCESS, notice));
    }

    /**
     * [getAllNotices] 모든 공지사항 조회
     * @param groupName 그룹 이름
     * @return 공지사항 응답 DTO 리스트
     */
    @GetMapping("/{groupName}/notice")
    public ResponseEntity<ResponseDataDto<List<EnterFeedResponseDto>>> getAllNotices(@PathVariable String groupName) {
        List<EnterFeedResponseDto> notices = enterFeedService.getAllNotices(groupName);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.NOTICE_RETRIEVE_SUCCESS, notices));
    }

    /**
     * [updateNotice] 공지사항 수정
     * @param groupName 그룹 이름
     * @param feedId 피드 ID
     * @param request 요청 객체
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @PatchMapping("/{groupName}/notice/{feedId}")
    public ResponseEntity<ResponseMessageDto> updateNotice(
            @PathVariable String groupName,
            @PathVariable String feedId,
            @RequestBody EnterFeedRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        enterFeedService.updateNotice(groupName, feedId, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.NOTICE_UPDATE_SUCCESS));
    }

    /**
     * [deleteNotice] 공지사항 삭제
     * @param groupName 그룹 이름
     * @param feedId 피드 ID
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @DeleteMapping("/{groupName}/notice/{feedId}")
    public ResponseEntity<ResponseMessageDto> deleteNotice(
            @PathVariable String groupName,
            @PathVariable String feedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        enterFeedService.deleteNotice(groupName, feedId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.NOTICE_DELETE_SUCCESS));
    }

}