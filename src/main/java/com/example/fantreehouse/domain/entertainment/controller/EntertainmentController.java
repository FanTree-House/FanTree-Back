package com.example.fantreehouse.domain.entertainment.controller;


import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentRequestDto;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentResponseDto;
import com.example.fantreehouse.domain.entertainment.service.EntertainmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.fantreehouse.common.enums.ErrorType.OVER_LOAD;

@RestController
@AllArgsConstructor
@RequestMapping("/enter")
public class EntertainmentController {

    private EntertainmentService entertainmentService;

    /**
     * 엔터테이먼트 계정 생성
     * @param enterRequestDto
     * @param userDetails
     * @return
     */
    @PostMapping
    public ResponseEntity<ResponseMessageDto> createEnter(
            @RequestPart(value = "file") MultipartFile file,
            @Valid @RequestPart EntertainmentRequestDto enterRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new S3Exception(OVER_LOAD);
        }
        entertainmentService.createEnter(file, enterRequestDto , userDetails.getUser().getId());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ENTERTAINMENT_CREATE_SUCCESS));
    }

    /**
     * 엔터테이먼트 계정 조회
     * @param enterName
     * @param userDetails
     * @return
     */
    @GetMapping("/{enterName}")
    public ResponseEntity<ResponseDataDto<EntertainmentResponseDto>> getEnter(
            @PathVariable String enterName,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        EntertainmentResponseDto responseDto = entertainmentService.getEnter(enterName, userDetails.getUser());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ENTERTAINMENT_READ_SUCCESS, responseDto));
    }

    /**
     * 엔터테이먼트 계정 수정
     * @param enterName
     * @param enterRequestDto
     * @param userDetails
     * @return
     */
    @PatchMapping("/{enterName}")
    public ResponseEntity<ResponseMessageDto> updateEnter(
            @RequestPart(value = "file") MultipartFile file,
            @PathVariable String enterName,
            @RequestPart EntertainmentRequestDto enterRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new S3Exception(OVER_LOAD);
        }
        entertainmentService.updateEnter(file, enterName, enterRequestDto , userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ENTERTAINMENT_UPDATAE_SUCCESS));
    }

    /**
     * 엔터테이먼트 계정 삭제
     * @param enterName
     * @param userDetails
     * @return
     */
    @DeleteMapping("/{enterName}")
    public ResponseEntity<ResponseMessageDto> deleteEnter(
            @PathVariable String enterName,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        entertainmentService.deleteEnter(enterName, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ENTERTAINMENT_DELETE_SUCCESS));
    }

}
