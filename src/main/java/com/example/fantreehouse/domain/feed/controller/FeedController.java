package com.example.fantreehouse.domain.feed.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artist.service.ArtistService;
import com.example.fantreehouse.domain.entertainment.service.EntertainmentService;
import com.example.fantreehouse.domain.feed.dto.FeedRequestDto;
import com.example.fantreehouse.domain.feed.entity.FeedEnumCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.fantreehouse.common.exception.errorcode.CommonErrorCode.INVALID_PARAMETER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/www.fantree.com/{group_name}")
public class FeedController {

    private final ArtistService artistService;
//    private final EntertainmentService entertainmentService;

    @PostMapping
    public ResponseEntity<ResponseDataDto<?>> createFeed (
            @PathVariable String groupName,
            @RequestParam(value = "category") FeedEnumCategory category,
            @AuthenticationPrincipal UserDetailsImpl UserDetails,
            @Valid @RequestBody final FeedRequestDto request
    ) {
        ResponseDataDto<?> responseDto;
        switch(category) {
            case ARTIST -> responseDto = artistService.createFeed(groupName, UserDetails,request );
//            case NOTICE -> responseDto = entertainmentService.createNotice(groupName, UserDetails, request);
//            case SCHEDULE -> responseDto = entertainmentService.createSchedule(groupName, UserDetails,request);
            default -> throw new IllegalArgumentException();
        }

        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.CREATED, responseDto));
    }
}
