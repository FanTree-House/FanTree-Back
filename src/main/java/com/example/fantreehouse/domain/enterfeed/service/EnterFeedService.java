package com.example.fantreehouse.domain.enterfeed.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedRequestDto;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedResponseDto;
import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
import com.example.fantreehouse.domain.enterfeed.repository.EnterFeedRepository;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.entertainment.repository.EntertainmentRepository;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnterFeedService {

    private final EnterFeedRepository enterFeedRepository;
    private final EntertainmentRepository entertainmentRepository;
    private final ArtistGroupRepository artistGroupRepository;

    @Autowired
    public EnterFeedService(EnterFeedRepository enterFeedRepository,
                            EntertainmentRepository entertainmentRepository,
                            ArtistGroupRepository artistGroupRepository) {
        this.enterFeedRepository = enterFeedRepository;
        this.entertainmentRepository = entertainmentRepository;
        this.artistGroupRepository = artistGroupRepository;
    }

    @Transactional
    public void createFeed(String groupName, EnterFeedRequestDto request, User user) {
        verifyEntertainmentAuthority(user);

        ArtistGroup artistGroup = artistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new CustomException(ErrorType.ARTIST_GROUP_NOT_FOUND));

        Entertainment entertainment = artistGroup.getEntertainment();

        EnterFeed enterFeed = new EnterFeed(
                entertainment,
                artistGroup,
                user,
                request.getTitle(),
                request.getContents(),
                request.getPostPicture(),
                request.getCategory(),
                request.getDate()
        );

        enterFeedRepository.save(enterFeed);
    }

    public EnterFeedResponseDto getFeed(String groupName, Long feedId, FeedCategory category) {
        EnterFeed enterFeed = getEnterFeed(groupName, feedId, category);
        return convertToResponseDto(enterFeed);
    }

    public List<EnterFeedResponseDto> getAllFeeds(String groupName, FeedCategory category) {
        List<EnterFeed> enterFeeds = getAllEnterFeeds(groupName, category);
        return enterFeeds.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateFeed(String groupName, Long feedId, EnterFeedRequestDto request, User user) {
        verifyEntertainmentAuthority(user);

        EnterFeed enterFeed = getEnterFeed(groupName, feedId, request.getCategory());

        enterFeed.updateContents(
                request.getTitle(),
                request.getContents(),
                request.getPostPicture(),
                request.getCategory(),
                request.getDate()
        );

        enterFeedRepository.save(enterFeed);
    }

    @Transactional
    public void deleteFeed(String groupName, Long feedId, User user, FeedCategory category) {
        verifyEntertainmentAuthority(user);

        EnterFeed enterFeed = getEnterFeed(groupName, feedId, category);
        enterFeedRepository.delete(enterFeed);
    }

    private void verifyEntertainmentAuthority(User user) {
        if (!UserRoleEnum.ENTERTAINMENT.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.NOT_FOUND_ENTER);
        }
    }

    private EnterFeed getEnterFeed(String groupName, Long feedId, FeedCategory category) {
        return enterFeedRepository.findByIdAndArtistGroupGroupNameAndCategory(feedId, groupName, category)
                .orElseThrow(() -> new CustomException(ErrorType.ENTER_FEED_NOT_FOUND));
    }

    private List<EnterFeed> getAllEnterFeeds(String groupName, FeedCategory category) {
        return enterFeedRepository.findAllByArtistGroupGroupNameAndCategory(groupName, category);
    }

    private EnterFeedResponseDto convertToResponseDto(EnterFeed enterFeed) {
        return new EnterFeedResponseDto(
                enterFeed.getId(),
                enterFeed.getTitle(),
                enterFeed.getContents(),
                enterFeed.getPostPicture(),
                enterFeed.getCategory(),
                enterFeed.getDate()
        );
    }
}