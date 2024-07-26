package com.example.fantreehouse.domain.subscription.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.subscription.dto.SubscriptionResponseDto;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.subscription.repository.SubscriptionRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final SubscriptionRepository subcriptionRepository;

    //  구독생성
    @Transactional
    public Subscription createSubscript(Long userId, String groupName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);

        subcriptionRepository.findByUser_IdAndArtistGroup_Id(user.getId(), artistGroup.getId()).ifPresent(
                e -> {
                    throw new CustomException(ErrorType.DUPLICATE_USER);
                });

        Subscription subscription = new Subscription(user, artistGroup);
        return subcriptionRepository.save(subscription);
    }

    //구독해지
    @Transactional
    public void deleteSubscript(Long userId, String groupName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);

        Subscription subscription = subcriptionRepository.findByUser_IdAndArtistGroup_Id(user.getId(), artistGroup.getId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_SUBSCRIPT_USER));

        if (!subscription.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.DUPLICATE_USER);
        }
        subcriptionRepository.delete(subscription);
    }

    //구독리스트 조회
    public List<SubscriptionResponseDto> findAllSubscript(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));

        List<Subscription> subscriptionList = subcriptionRepository.findAllByUserId(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_SUBSCRIPT_USER));

        if (subscriptionList.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_SUBSCRIPTION);
        }
        return subscriptionList.stream()
                .map(SubscriptionResponseDto::new)
                .toList();
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));
    }

    public ArtistGroup findArtistGroup(String groupName) {
        return artistGroupRepository.findByGroupName(groupName).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_ARTISTGROUP));
    }
}