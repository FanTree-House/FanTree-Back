package com.example.fantreehouse.domain.subscription.dto;

import com.example.fantreehouse.domain.subscription.entity.Subscription;

public class SubscriptionResponseDto {

    private Long id;
    private String group_name;

    public SubscriptionResponseDto(Subscription subscription) {
        this.id = subscription.getId();
        this.group_name = subscription.getUser().getArtist().getArtistGroup().getGroupName();
    }
}
