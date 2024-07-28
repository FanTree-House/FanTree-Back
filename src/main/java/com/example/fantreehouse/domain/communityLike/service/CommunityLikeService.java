package com.example.fantreehouse.domain.communityLike.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.communityLike.entitiy.CommunityLike;
import com.example.fantreehouse.domain.communityLike.repository.CommunityLikeRepository;
import com.example.fantreehouse.domain.communitycomment.repository.CommunityCommentRepository;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.communityfeed.repository.CommunityFeedRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityLikeService {
    private final CommunityLikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommunityFeedRepository feedRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final CommunityCommentRepository commentRepository;

    public CommunityLike pressFeedLike(Long userId, Long feedId, String groupName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);
        CommunityFeed feed = findFeed(feedId);

        CommunityLike communityLike = new CommunityLike(user, feed);

        return null;
    }










    //유저찾기
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));
    }

    //피드찾기
    public CommunityFeed findFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_USER_FEED));
    }

    //아티스트 그룹찾기
    public ArtistGroup findArtistGroup(String groupName) {
        return artistGroupRepository.findByGroupName(groupName).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_ARTISTGROUP));
    }

}
