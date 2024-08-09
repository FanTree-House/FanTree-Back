//package com.example.fantreehouse;
//
//import com.example.fantreehouse.domain.artist.entity.Artist;
//import com.example.fantreehouse.domain.artist.repository.ArtistRepository;
//import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
//import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
//import com.example.fantreehouse.domain.comment.repository.CommentRepository;
//import com.example.fantreehouse.domain.commentLike.repository.CommentLikeRepository;
//import com.example.fantreehouse.domain.communityLike.repository.CommunityLikeRepository;
//import com.example.fantreehouse.domain.communitycomment.repository.CommunityCommentRepository;
//import com.example.fantreehouse.domain.communityfeed.repository.CommunityFeedRepository;
//import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
//import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
//import com.example.fantreehouse.domain.enterfeed.repository.EnterFeedRepository;
//import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
//import com.example.fantreehouse.domain.entertainment.repository.EntertainmentRepository;
//import com.example.fantreehouse.domain.feed.repository.FeedRepository;
//import com.example.fantreehouse.domain.feedlike.repository.FeedLikeRepository;
//import com.example.fantreehouse.domain.product.product.repository.ProductRepository;
//import com.example.fantreehouse.domain.subscription.repository.SubscriptionRepository;
//import com.example.fantreehouse.domain.user.entity.User;
//import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
//import com.example.fantreehouse.domain.user.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class InitData {
//  private final UserRepository userRepository;
//  private final ProductRepository productRepository;
//  private final PasswordEncoder passwordEncoder;
//  private final SubscriptionRepository subscriptionRepository;
//  private final FeedLikeRepository feedLikeRepository;
//  private final FeedRepository feedRepository;
//  private final EntertainmentRepository entertainmentRepository;
//  private final EnterFeedRepository enterFeedRepository;
//  private final CommunityLikeRepository communityLikeRepository;
//  private final CommunityFeedRepository communityFeedRepository;
//  private final CommunityCommentRepository communityCommentRepository;
//  private final CommentLikeRepository commentLikeRepository;
//  private final CommentRepository commentRepository;
//  private final ArtistGroupRepository artistGroupRepository;
//  private final ArtistRepository artistRepository;
//
//  @Transactional
//  @PostConstruct
//  public void init(){
//
//     User user1 = new User("ModhauseManager", "모드하우스", "Modhaus", "modhausmanager@gmail.com",
//          passwordEncoder.encode("12345678"), UserRoleEnum.ENTERTAINMENT);
//
//     User user2 = new User("SMenterManger", "에스엠엔터", "SM Entertainment", "smentermanager@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ENTERTAINMENT);
//
//     User user3 = new User("tripleS1", "윤서연", "서연", "ModhausS1@gmail.com",
//          passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//     User user4 = new User("tripleS2", "정혜린", "혜린", "ModhausS2@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user5 = new User("tripleS3", "이지우", "지우", "ModhausS3@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user6 = new User("tripleS4", "김채연", "채연", "ModhausS4@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user7 = new User("tripleS5", "김유연", "유연", "ModhausS5@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user8 = new User("tripleS6", "김수민", "수민", "ModhausS6@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user9 = new User("tripleS7", "김나경", "나경", "ModhausS7@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user10 = new User("tripleS8", "공유빈", "유빈", "ModhausS8@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user11 = new User("tripleS9", "야마다 카에데", "카에데", "ModhausS9@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user12 = new User("tripleS10", "서다현", "다현", "ModhausS10@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user13 = new User("tripleS11", "카미모토 코토네", "코토네", "ModhausS11@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user14 = new User("tripleS12", "곽연지", "연지", "ModhausS12@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user15 = new User("tripleS13", "쉬니엔츠", "니엔", "ModhausS13@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user16 = new User("tripleS14", "박소현", "소현", "ModhausS14@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user17 = new User("tripleS15", "저우신위", "신위", "ModhausS15@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user18 = new User("tripleS16", "코우마 마유", "마유", "ModhausS16@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user19 = new User("tripleS17", "카와가미 린", "린", "ModhausS17@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user20 = new User("tripleS18", "주빈", "주빈", "ModhausS18@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user21 = new User("tripleS19", "정하연", "하연", "ModhausS19@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user22 = new User("tripleS20", "박시온", "시온", "ModhausS20@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user23 = new User("tripleS21", "김채원", "채원", "ModhausS21@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user24 = new User("tripleS22", "정해린", "서아", "ModhausS22@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user25 = new User("tripleS23", "피라다 분락사", "설린", "ModhausS23@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user26 = new User("tripleS24", "지서연", "서연", "ModhausS24@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user27 = new User("SMaespa1", "유지민", "카리나", "SM-aespaK@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user28 = new User("SMaespa2", "우치나가 애리", "지젤", "SM-aespaG@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user29 = new User("SMaespa3", "김민정", "윈터", "SM-aespaW@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    User user30 = new User("SMaespa4", "닝이줘", "닝닝", "SM-aespaN@gmail.com",
//              passwordEncoder.encode("12345678"), UserRoleEnum.ARTIST);
//
//    List<User> userList = List.of(user1, user2, user3, user4, user5, user6, user7, user8,
//        user9, user10, user11, user12, user13, user14, user15, user16, user17, user18, user19, user20,
//        user21, user22, user23, user24, user25, user26, user27, user28, user29, user30);
//    userRepository.saveAll(userList);
//
//     Entertainment entertainment = new Entertainment("SM Entertainment", 102951344L, user2);
//     Entertainment entertainment1 = new Entertainment("Modhaus",1534122412L, user1);
//
//     List<Entertainment> entertainmentList = List.of(entertainment, entertainment1);
//     entertainmentRepository.saveAll(entertainmentList);
//
//     ArtistGroup artistGroup = new ArtistGroup("aespa","null"
//         ,"SM 4인 다국적 걸그룹",entertainment,"SM");
//
//    ArtistGroup artistGroup1 = new ArtistGroup("tripleS","null"
//        ,"걸그룹 최초 24인조 그룹", entertainment1 ,"Modhaus");
//
//    List<ArtistGroup> artistGroupList = List.of(artistGroup,artistGroup1);
//    artistGroupRepository.saveAll(artistGroupList);
//
//    Artist artist = new Artist("서연", "",
//        "WAV들 안녕~!",artistGroup1,user3);
//    Artist artist1 = new Artist("혜린", "",
//        "WAV들 안녕~!",artistGroup1,user4);
//    Artist artist2 = new Artist("지우", "",
//        "WAV들 안녕~!",artistGroup1,user5);
//    Artist artist3 = new Artist("채연", "",
//        "WAV들 안녕~!",artistGroup1,user6);
//    Artist artist4 = new Artist("유연", "",
//        "WAV들 안녕~!",artistGroup1,user7);
//    Artist artist5 = new Artist("수민", "",
//        "WAV들 안녕~!",artistGroup1,user8);
//    Artist artist6 = new Artist("나경", "",
//        "WAV들 안녕~!",artistGroup1,user9);
//    Artist artist7 = new Artist("유빈", "",
//        "WAV들 안녕~!",artistGroup1,user10);
//    Artist artist8 = new Artist("카에데", "",
//        "WAV들 안녕~!",artistGroup1,user11);
//    Artist artist9 = new Artist("다현", "",
//        "WAV들 안녕~!",artistGroup1,user12);
//    Artist artist10 = new Artist("코토네", "",
//        "WAV들 안녕~!",artistGroup1,user13);
//    Artist artist11 = new Artist("연지", "",
//        "WAV들 안녕~!",artistGroup1,user14);
//    Artist artist12 = new Artist("니엔", "",
//        "WAV들 안녕~!",artistGroup1,user15);
//    Artist artist13 = new Artist("소현", "",
//        "WAV들 안녕~!",artistGroup1,user16);
//    Artist artist14 = new Artist("신위", "",
//        "WAV들 안녕~!",artistGroup1,user17);
//    Artist artist15 = new Artist("마유", "",
//        "WAV들 안녕~!",artistGroup1,user18);
//    Artist artist16 = new Artist("린", "",
//        "WAV들 안녕~!",artistGroup1,user19);
//    Artist artist17 = new Artist("주빈", "",
//        "WAV들 안녕~!",artistGroup1,user20);
//    Artist artist18 = new Artist("하연", "",
//        "WAV들 안녕~!",artistGroup1,user21);
//    Artist artist19 = new Artist("시온", "",
//        "WAV들 안녕~!",artistGroup1,user22);
//    Artist artist20 = new Artist("채원", "",
//        "WAV들 안녕~!",artistGroup1,user23);
//    Artist artist21 = new Artist("서아", "",
//        "WAV들 안녕~!",artistGroup1,user24);
//    Artist artist22 = new Artist("설린", "",
//        "WAV들 안녕~!",artistGroup1,user25);
//    Artist artist23 = new Artist("지연", "",
//        "WAV들 안녕~!",artistGroup1,user26);
//
//    Artist artist24 = new Artist("카리나", "",
//        "수수수퍼 노바",artistGroup,user27);
//    Artist artist25 = new Artist("지젤", "",
//        "수수수퍼 노바",artistGroup,user28);
//    Artist artist26 = new Artist("윈터", "",
//        "수수수퍼 노바",artistGroup,user29);
//    Artist artist27 = new Artist("닝닝", "",
//        "수수수퍼 노바",artistGroup,user30);
//
//
//    List<Artist> artistList = List.of(
//        artist, artist1, artist2, artist3, artist4, artist5, artist6, artist7,
//        artist8, artist9, artist10, artist11, artist12, artist13, artist14, artist15, artist16,
//        artist17, artist18, artist19, artist20, artist21, artist22, artist23, artist24, artist25,
//        artist26, artist27
//    );
//    artistRepository.saveAll(artistList);
//
//    EnterFeed smEnterFeed = new EnterFeed(entertainment,user2,"aespa 스케쥴 공지사항 입니다.",
//        "스케쥴이 변동되었습니다.",entertainment.getEnterName(), FeedCategory.NOTICE, LocalDate.now(),
//        artistGroup, artistGroup.getGroupName());
//
//    enterFeedRepository.save(smEnterFeed);
//  }
//}
