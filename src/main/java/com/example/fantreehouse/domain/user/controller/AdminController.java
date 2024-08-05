package com.example.fantreehouse.domain.user.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.user.dto.AdminRequestDto;
import com.example.fantreehouse.domain.user.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/blacklist")
    private ResponseEntity<ResponseMessageDto> transBlacklist(
            @RequestBody AdminRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        adminService.transBlacklist(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.TRANSFORM_BLACKLIST_SUCCESS));
    }

    @PatchMapping("/role")
    private ResponseEntity<ResponseMessageDto> transRole(
            @RequestBody AdminRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        adminService.transRole(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_ROLE_UPDATE_SUCCESS));
    }
}
