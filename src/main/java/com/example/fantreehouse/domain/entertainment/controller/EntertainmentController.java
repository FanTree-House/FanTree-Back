package com.example.fantreehouse.domain.entertainment.controller;


import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentRequestDto;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentResponseDto;
import com.example.fantreehouse.domain.entertainment.service.EntertainmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/enter")
public class EntertainmentController {

    private EntertainmentService entertainmentService;

    /**
     * 엔터테이먼트 계정 생성
     */
    @PutMapping
    public ResponseEntity<ResponseMessageDto> createEntertainment(
            @Valid @RequestBody EntertainmentRequestDto enterRequestDto
            /*@AuthenticationPrincipal UserDetailsImpl userDetails*/) {
        entertainmentService.createEnter(enterRequestDto /*, userDetails.getUser()*/);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ENTERTAINMENT_CREATE_SUCCESS));
    }

    /**
     * 엔터테이먼트 계정 조회
     */
//    @GetMapping
//    public ResponseDataDto

    /**
     * 엔터테이먼트 계정 수정
     */
//    @PatchMapping
//   public ResponseMessageDto

    /**
     * 엔터테이먼트 계정 삭제
     */
//    @DeleteMapping
//    public ResponseMessageDto

}
