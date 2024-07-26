package com.example.fantreehouse.domain.subscription.dto;

import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class SubscriptionResponseDto {

    private Long id;
    private String group_name;

    public SubscriptionResponseDto(Subscription subscription ) {
        this.id = subscription.getId();
        this.group_name = subscription.getArtistGroup().getGroupName();
    }
}
