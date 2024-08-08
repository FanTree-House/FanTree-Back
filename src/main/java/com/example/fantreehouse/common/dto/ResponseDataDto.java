package com.example.fantreehouse.common.dto;

import com.example.fantreehouse.common.enums.ResponseStatus;
import lombok.Getter;

@Getter
public class ResponseDataDto<T> {
    private ResponseStatus status;
    private String message;
    private T data;

    public ResponseDataDto(ResponseStatus responseStatus, T data) {
        this.status = responseStatus;
        this.message = responseStatus.getMessage();
        this.data = data;
        //상태를 담아주기
    }

    // 사용 예시
    // return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.CARD_UPDATE_SUCCESS, responseDto));
}
