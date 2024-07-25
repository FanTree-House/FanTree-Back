package com.example.fantreehouse.domain.subscription.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.subscription.dto.SubscriptionResponseDto;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artistGroup/subscript")
@RequiredArgsConstructor

public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    /**
     * 구독하기
     * @param userDetails
     * @param group_name
     * @return
     */
    @PostMapping("/{group_name}")
    public ResponseEntity<ResponseMessageDto> createSubscript(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable String group_name
                                                          ) {
        subscriptionService.createSubscript(userDetails.getUser(), group_name);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_SUCCESS_SUBSCRIPT));
    }

    /**
     * 구독해지하기
     * @param userDetails
     * @param group_name
     * @param subscription
     * @return
     */
    @DeleteMapping("/{group_name}")
    public ResponseEntity<ResponseMessageDto> deleteSubscript(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable String group_name,
                                                              Subscription subscription) {
        subscriptionService.deleteSubscript(userDetails.getUser(), group_name, subscription);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_SUCCESS_SUBSCRIPT));
    }

    @GetMapping
    public ResponseEntity<?> findAllSubscript(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<SubscriptionResponseDto> responseDto = subscriptionService.findAllSubscript(userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }

}
