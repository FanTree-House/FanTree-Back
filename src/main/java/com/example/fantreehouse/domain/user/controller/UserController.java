package com.example.fantreehouse.domain.user.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.domain.user.dto.LoginRequestDto;
import com.example.fantreehouse.domain.user.dto.SignUpRequestDto;
import com.example.fantreehouse.domain.user.service.UserSerive;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

public class UserController {
<<<<<<< Updated upstream
    private UserSerive userSerive;


//    @PostMapping
//    public ResponseEntity<ResponseMessageDto> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
//        userService.signUp(requestDto);
//        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SIGNUP_SUCCESS));
//    }
//
//    @PutMapping("/withDraw/{id}")
//    public ResponseEntity<ResponseMessageDto> withDraw(@PathVariable Long id, String password) {
//        userService.withDraw(id, password);
//        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.WITHDRAW_SUCCESS));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<ResponseMessageDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
//        userService.login(requestDto);
//        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.LOGIN_SUCCESS));
//    }
//
//    @PutMapping("/logout/{id}")
//    public ResponseEntity<ResponseMessageDto> logout(@PathVariable Long id) {
//        userService.logout(id);
//        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.LOGOUT_SUCCESS));
//    }
//}
}
=======
}
>>>>>>> Stashed changes
