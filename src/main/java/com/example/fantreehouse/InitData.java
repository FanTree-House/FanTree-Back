//package com.example.fantreehouse;
//
//import com.example.fantreehouse.domain.artist.entity.Artist;
//import com.example.fantreehouse.domain.artist.repository.ArtistRepository;
//import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
//import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
//import com.example.fantreehouse.domain.comment.repository.CommentRepository;
//import com.example.fantreehouse.domain.commentLike.repository.CommentLikeRepository;
//import com.example.fantreehouse.domain.communityLike.entitiy.CommunityLike;
//import com.example.fantreehouse.domain.communityLike.repository.CommunityLikeRepository;
//import com.example.fantreehouse.domain.communitycomment.repository.CommunityCommentRepository;
//import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedRequestDto;
//import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedResponseDto;
//import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
//import com.example.fantreehouse.domain.communityfeed.repository.CommunityFeedRepository;
//import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
//import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
//import com.example.fantreehouse.domain.enterfeed.repository.EnterFeedRepository;
//import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
//import com.example.fantreehouse.domain.entertainment.repository.EntertainmentRepository;
//import com.example.fantreehouse.domain.feed.entity.Feed;
//import com.example.fantreehouse.domain.feed.repository.FeedRepository;
//import com.example.fantreehouse.domain.feedlike.entity.FeedLike;
//import com.example.fantreehouse.domain.feedlike.repository.FeedLikeRepository;
//import com.example.fantreehouse.domain.subscription.entity.Subscription;
//import com.example.fantreehouse.domain.subscription.repository.SubscriptionRepository;
//import com.example.fantreehouse.domain.user.entity.User;
//import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
//import com.example.fantreehouse.domain.user.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.example.fantreehouse.domain.s3.service.S3FileUploader.START_PROFILE_URL;
//
//@Component
//@RequiredArgsConstructor
//public class InitData {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final SubscriptionRepository subscriptionRepository;
//    private final FeedLikeRepository feedLikeRepository;
//    private final FeedRepository feedRepository;
//    private final EntertainmentRepository entertainmentRepository;
//    private final EnterFeedRepository enterFeedRepository;
//    private final CommunityLikeRepository communityLikeRepository;
//    private final CommunityFeedRepository communityFeedRepository;
//    private final CommunityCommentRepository communityCommentRepository;
//    private final CommentLikeRepository commentLikeRepository;
//    private final CommentRepository commentRepository;
//    private final ArtistGroupRepository artistGroupRepository;
//    private final ArtistRepository artistRepository;
//
//    @Transactional
//    @PostConstruct
//    public void init() {
//
//        //엔터
//        User user1 = new User("JYPEnter", "제이와이피엔터", "JYPEntertainment", "Jypmanager@gmail.com",
//                passwordEncoder.encode("enter123!"), UserRoleEnum.ENTERTAINMENT);
//
//        User user2 = new User("PLEDISEnter", "플레디스엔터", "PLEDISEntertainment", "PLEDISEntertainment@gmail.com",
//                passwordEncoder.encode("enter123!"), UserRoleEnum.ENTERTAINMENT);
//
//        //아티스트 세븐틴
//        User user3 = new User("SEVENTEEN", "최승철", "에스쿱스", "seventeen1@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user4 = new User("SEVENTEEN1", "정한", "정한", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user5 = new User("SEVENTEEN2", "조슈아", "조슈아", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user6 = new User("SEVENTEEN3", "준", "준", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user7 = new User("SEVENTEEN4", "호시", "호시", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user8 = new User("SEVENTEEN5", "원우", "원우", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user9 = new User("SEVENTEEN6", "우지", "우지", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user10 = new User("SEVENTEEN7", "디에잇", "디에잇", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user11 = new User("SEVENTEEN8", "디에잇", "디에잇", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user12 = new User("SEVENTEEN9", "민규", "민규", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user13 = new User("SEVENTEEN10", "도겸", "도겸", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user14 = new User("SEVENTEEN11", "승관", "승관", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user15 = new User("SEVENTEEN12", "버논", "버논", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user16 = new User("SEVENTEEN13", "디노", "디노", "seventeen2@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//
//        //아티스트 잇지
//        User user17 = new User("ITZY", "예지", "예지", "ITZY1@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user18 = new User("ITZY1", "리아", "리아", "ITZY1@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user19 = new User("ITZY2", "류진", "류진", "ITZY1@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user20 = new User("ITZY3", "채령", "채령", "ITZY1@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user21 = new User("ITZY4", "유나", "유나", "ITZY1@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//
//        // 아티스트 데이식스
//        User user22 = new User("DAY6", "성진", "성진", "DAY6@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user23 = new User("DAY62", "영케이", "Young K", "DAY6@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user24 = new User("DAY63", "원필", "원필", "DAY6@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//        User user25 = new User("DAY64", "도운", "도운", "DAY6@gmail.com",
//                passwordEncoder.encode("artist123!"), UserRoleEnum.ARTIST);
//
//
//        //엔터, 아티스트, 유저 저장
//        List<User> userList = List.of(user1, user2, user3, user4, user5, user6, user7, user8,
//                user9, user10, user11, user12, user13, user14, user15, user16, user17, user18, user19, user20,
//                user21, user22, user23, user24, user25);
//        userRepository.saveAll(userList);
//
//        //엔터계정 생성
//        Entertainment entertainment1 = new Entertainment("JYP Entertainment", 102951344L, user1);
//        Entertainment entertainment2 = new Entertainment("PLEDIS Entertainment", 1534122412L, user2);
//
//        List<Entertainment> entertainmentList = List.of(entertainment1, entertainment2);
//        entertainmentRepository.saveAll(entertainmentList);
//
//        //아티스트그룹 생성
//        ArtistGroup artistGroup1 = new ArtistGroup("SEVENTEEN", "null"
//                , "2015년 5월 26일 데뷔한 플레디스 엔터테인먼트 소속 대한민국의 13인조 다국적 보이그룹", entertainment2, "PLEDIS Entertainment");
//
//        ArtistGroup artistGroup2 = new ArtistGroup("ITZY", "null"
//                , "2019년 2월 12일에 데뷔한 JYP엔터테인먼트 소속 5인조 걸그룹", entertainment1, "JYP Entertainment");
//
//        ArtistGroup artistGroup3 = new ArtistGroup("DAY6", "null"
//                , "2015년 9월 7일에 데뷔한 JYP엔터테인먼트 소속 4인조 밴드. JYP에서 밴드로 데뷔한 최초의 아티스트.", entertainment1, "JYP Entertainment");
//
//        List<ArtistGroup> artistGroupList = List.of(artistGroup1, artistGroup2, artistGroup3);
//        artistGroupRepository.saveAll(artistGroupList);

//        //아티스트 계정 생성
//        Artist artist = new Artist("서연", "",
//                "WAV들 안녕~!", artistGroup2, user3);
//        Artist artist1 = new Artist("혜린", "",
//                "WAV들 안녕~!", artistGroup2, user4);
//        Artist artist2 = new Artist("지우", "",
//                "WAV들 안녕~!", artistGroup2, user5);
//        Artist artist3 = new Artist("채연", "",
//                "WAV들 안녕~!", artistGroup2, user6);
//        Artist artist4 = new Artist("유연", "",
//                "WAV들 안녕~!", artistGroup2, user7);
//
//
//        Artist artist24 = new Artist("카리나", "",
//                "수수수퍼 노바", artistGroup1, user27);
//        Artist artist25 = new Artist("지젤", "",
//                "수수수퍼 노바", artistGroup1, user28);
//        Artist artist26 = new Artist("윈터", "",
//                "수수수퍼 노바", artistGroup1, user29);
//        Artist artist27 = new Artist("닝닝", "",
//                "수수수퍼 노바", artistGroup1, user30);
//
//
//        List<Artist> artistList = List.of(
//                artist, artist1, artist2, artist3, artist4, artist5, artist6, artist7,
//                artist8, artist9, artist10, artist11, artist12, artist13, artist14, artist15, artist16,
//                artist17, artist18, artist19, artist20, artist21, artist22, artist23, artist24, artist25,
//                artist26, artist27
//        );
//        artistRepository.saveAll(artistList);
//
//        //엔터피드 생성
//        EnterFeed smEnterFeed = new EnterFeed(entertainment1, user2, "aespa 스케쥴 공지사항 입니다.",
//                "스케쥴이 변동되었습니다.", FeedCategory.NOTICE, LocalDate.now());
//
//        enterFeedRepository.save(smEnterFeed);
//
//        //구독 생성
//        Subscription subscription1 = new Subscription(user31, artistGroup1);
//        Subscription subscription2 = new Subscription(user32, artistGroup2);
//        subscriptionRepository.save(subscription1);
//        subscriptionRepository.save(subscription2);
//
//        //아티스트 피드 생성
//        List<String> imageUrl1 = new ArrayList<>();
//        imageUrl1.add(START_PROFILE_URL);
//        imageUrl1.add(START_PROFILE_URL);
//        Feed feed1 = new Feed("카리나", "안녕하세요.", imageUrl1, user27, artistGroup1);
//        Feed feed2 = new Feed("카리나", "반갑습니다.", imageUrl1, user27, artistGroup1);
//        Feed feed3 = new Feed("카리나", "컴백했어요.", imageUrl1, user27, artistGroup1);
//        feedRepository.save(feed1);
//        feedRepository.save(feed2);
//        feedRepository.save(feed3);
//
//
//        //피드 좋아요 추가
//        FeedLike feedLike1 = new FeedLike(feed1, user31);
//        FeedLike feedLike2 = new FeedLike(feed2, user31);
//        FeedLike feedLike3 = new FeedLike(feed3, user31);
//        FeedLike feedLike4 = new FeedLike(feed3, user32);
//        feedLikeRepository.save(feedLike1);
//        feedLikeRepository.save(feedLike2);
//        feedLikeRepository.save(feedLike3);
//        feedLikeRepository.save(feedLike4);
//
//        //커뮤게시글 생성
//        List<String> imageUrl2 = new ArrayList<>();
//        imageUrl2.add(START_PROFILE_URL);
//        imageUrl2.add(START_PROFILE_URL);
//        CommunityFeed communityFeed1 = new CommunityFeed(
//                "카리나는 하트(❤\uFE0F), 지젤은 달(\uD83C\uDF19), 윈터는 별(⭐), 닝닝은 나비(\uD83E\uDD8B)",
//                "user31", imageUrl2, user31, artistGroup1);
//        CommunityFeed communityFeed2 = new CommunityFeed(
//                "지금은 자동 줄넘김과 스크롤바 생성여부에 대한 테스트를 진행하고 있습니다. 그야 지금 이순간만은 프론트 개발자니까요!",
//                        "user31", imageUrl2, user31, artistGroup1);
//        CommunityFeed communityFeed3 = new CommunityFeed(
//                "aespa의 스토리텔링에 대해서는 '현실 세계'에 존재하는 아티스트 멤버와 '가상 세계'에 존재하는 아바타 멤버가 현실과 가상의 중간 세계인 디지털 세계를 통해 소통하고 교감하며 성장해가는 스토리텔링을 가지고 있다.",
//                "user31", imageUrl2, user31, artistGroup1);
//        communityFeedRepository.save(communityFeed1);
//        communityFeedRepository.save(communityFeed2);
//        communityFeedRepository.save(communityFeed3);
//
//        //커뮤게시글 좋아요 추가
//        CommunityLike communityLike1 = new CommunityLike(user32, communityFeed1);
//        CommunityLike communityLike2 = new CommunityLike(user32, communityFeed2);
//        CommunityLike communityLike3 = new CommunityLike(user32, communityFeed3);
//        communityLikeRepository.save(communityLike1);
//        communityLikeRepository.save(communityLike2);
//        communityLikeRepository.save(communityLike3);
//
//    }
//}