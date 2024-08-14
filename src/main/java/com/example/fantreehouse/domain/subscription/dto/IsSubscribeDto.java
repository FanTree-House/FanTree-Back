package com.example.fantreehouse.domain.subscription.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IsSubscribeDto {
    private Boolean isSubscribe;

    public IsSubscribeDto(Boolean isSubscribe) {
        this.isSubscribe = isSubscribe;
    }
}
