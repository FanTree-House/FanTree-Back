package com.example.fantreehouse.domain.subscription.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.subscription.repository.SubcriptionRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final SubcriptionRepository subcriptionRepository;

//  구독생성
    public Subscription createSubscript(User user , String group_name) {
        user = userRepository.findById(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));

        ArtistGroup artistGroup = artistGroupRepository.findByGroupName(group_name).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_ARTISTGROUP));

        Subscription subscription = new Subscription();
        if (subscription.getUser().getArtist().getArtistGroup().getGroupName().equals(user.getArtist().getArtistGroup().getGroupName())) {
            throw new CustomException(ErrorType.DUPLICATE_USER);
        }
        return subcriptionRepository.save(subscription);
    }

    //구독해지
    public void deleteSubscript(User user, String group_name, Subscription subscription) {
        user = userRepository.findById(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));

        ArtistGroup artistGroup = artistGroupRepository.findByGroupName(group_name).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_ARTISTGROUP));

        subscription = subcriptionRepository.findById(subscription.getId()).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_SUBSCRIPT_USER));

        if (!subscription.getUser().getArtist().getArtistGroup().getGroupName().equals(user.getArtist().getArtistGroup().getGroupName())) {
            throw new CustomException(ErrorType.DUPLICATE_USER);
        }
        subcriptionRepository.delete(subscription);
    }
}
