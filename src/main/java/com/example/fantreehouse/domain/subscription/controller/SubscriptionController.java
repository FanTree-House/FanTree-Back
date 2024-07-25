package com.example.fantreehouse.domain.subscription.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artistGroup/subscript/{group_name}")
@RequiredArgsConstructor

public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<ResponseMessageDto> createSubscript(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable String group_name
                                                          ) {
        subscriptionService.createSubscript(userDetails.getUser(), group_name);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_SUCCESS_SUBSCRIPT));
    }

    @DeleteMapping
    public ResponseEntity<ResponseMessageDto> deleteSubscript(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable String group_name,
                                                              Subscription subscription) {
        subscriptionService.deleteSubscript(userDetails.getUser(), group_name, subscription);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_SUCCESS_SUBSCRIPT));
    }

}
