package com.example.fantreehouse.domain.user.controller;

import com.example.fantreehouse.domain.user.service.UserSerive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class UserController {
    private UserSerive userSerive;
}
