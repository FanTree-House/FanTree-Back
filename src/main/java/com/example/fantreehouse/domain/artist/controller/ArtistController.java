package com.example.fantreehouse.domain.artist.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artist.dto.request.ArtistRequestDto;
import com.example.fantreehouse.domain.artist.dto.response.ArtistProfileResponseDto;
import com.example.fantreehouse.domain.artist.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.fantreehouse.common.enums.ErrorType.OVER_LOAD;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService artistService;

    /**
     * 아티스트 계정 생성
     *
     * @param userDetails
     * @param requestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<ResponseMessageDto> createArtist(
            @RequestPart(value = "file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestPart final ArtistRequestDto requestDto
    ) {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new S3Exception(OVER_LOAD);
        }
        artistService.createArtist(userDetails, file, requestDto);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_CREATED));
    }

    /**
     * 아티스트 프로필 수정
     *
     * @param artistId
     * @param userDetails
     * @param requestDto
     * @return
     */
    @PatchMapping("/{artistId}")
    public ResponseEntity<ResponseMessageDto> updateArtist(
            @PathVariable Long artistId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(required = false) MultipartFile file,
            @Valid @RequestPart final ArtistRequestDto requestDto
    ) {
        if (file != null && file.getSize() > 10 * 1024 * 1024) {
            throw new S3Exception(OVER_LOAD);
        }
        artistService.updateArtist(artistId, userDetails, file, requestDto);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_UPDATED));
    }

    /**
     * 아티스트 단건 조회
     *
     * @param artistId
     * @return
     */
    @GetMapping("/{artistId}")
    public ResponseEntity<ResponseDataDto> getArtist(
            @PathVariable Long artistId
    ) {
        ArtistProfileResponseDto responseDto = artistService.getArtist(artistId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ARTIST_READ_SUCCESS, responseDto));
    }

    /**
     * 아티스트 전체 조회(6명씩, 구독자 순)/비가입자 가능
     *
     * @param page
     * @return
     */
    @GetMapping
    public ResponseEntity<ResponseDataDto> getAllArtist(
            @RequestParam int page
    ) {
        Page<ArtistProfileResponseDto> pageArtist = artistService.getAllArtist(page);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ARTIST_READ_SUCCESS, pageArtist));
    }

    /**
     * 아티스트 계정 삭제
     *
     * @param artistId
     * @param userDetails
     * @return
     */
    @DeleteMapping("/{artistId}")
    public ResponseEntity<ResponseMessageDto> deleteArtist(
            @PathVariable Long artistId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        artistService.deleteFeed(artistId, userDetails);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FEED_DELETED));
    }


}
