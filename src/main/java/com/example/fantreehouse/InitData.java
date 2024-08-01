package com.example.fantreehouse;

import com.example.fantreehouse.domain.artist.repository.ArtistRepository;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.comment.repository.CommentRepository;
import com.example.fantreehouse.domain.commentLike.repository.CommentLikeRepository;
import com.example.fantreehouse.domain.communityLike.repository.CommunityLikeRepository;
import com.example.fantreehouse.domain.communitycomment.repository.CommunityCommentRepository;
import com.example.fantreehouse.domain.communityfeed.repository.CommunityFeedRepository;
import com.example.fantreehouse.domain.enterfeed.repository.EnterFeedRepository;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.entertainment.repository.EntertainmentRepository;
import com.example.fantreehouse.domain.feed.repository.FeedRepository;
import com.example.fantreehouse.domain.feedlike.repository.FeedLikeRepository;
import com.example.fantreehouse.domain.product.product.repository.ProductRepository;
import com.example.fantreehouse.domain.subscription.repository.SubscriptionRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class InitData {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionRepository subscriptionRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final FeedRepository feedRepository;
    private final EntertainmentRepository entertainmentRepository;
    private final EnterFeedRepository enterFeedRepository;
    private final CommunityLikeRepository communityLikeRepository;
    private final CommunityFeedRepository communityFeedRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final ArtistRepository artistRepository;


    @Transactional
    @PostConstruct
    public void init() {

        List<ArtistGroup> artistGroupList = List.of(

                new ArtistGroup("aespa", "null"
                        , "SM 4인 다국적 걸그룹"
                        , new Entertainment(), "SM"),


                new ArtistGroup("tripleS", "null"
                        , "걸그룹 최초 24인조 그룹", new Entertainment(), "Modhaus")
        );

        List<Entertainment> entertainmentList = List.of(
                new Entertainment("SM"
                        , 102951344L
                        , "SMLOGO"
                        , new User()
                ),

                new Entertainment("Modhaus"
                        , 1534122412L
                        , "MODLOGO"
                        , new User()
                )
        );


    }


}
