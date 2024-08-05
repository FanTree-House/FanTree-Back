package com.example.fantreehouse.domain.s3.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.example.fantreehouse.common.enums.ErrorType.*;

@Component
public class S3FileUploaderUtil {

    private static final String URL_PREFIX = "Fantree";
    private static final String USER_PROFILE_DIR = "UserProfile";
    private static final String ARTIST_PROFILE_DIR = "ArtistProfile";
    private static final String ENTER_LOGO_DIR = "EnterLogo";
    private static final String ARTIST_GROUP_DIR = "ArtistGroupProfile";
    private static final String ENTER_FEED_DIR = "EnterFeed";
    private static final String ARTIST_FEED_DIR = "ArtistFeed";
    private static final String COMMUNITY_DIR = "Community";
    private static final String PRODUCT_DIR = "Product";

    public static boolean isFileExists(MultipartFile multipartFile) {
        return multipartFile != null && !multipartFile.isEmpty();
    }

    public static boolean areFilesExist(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) { // 리스트가 null 이거나 비어 있을 경우
            return false;
        }
        return files.stream().allMatch(file -> file != null && !file.isEmpty());
    }

    //getContentType() 이용한 확장자 확인
    public static void validateImageTypeWithContentType(MultipartFile file) {
        String contentType = file.getContentType(); //postman 이 원인일 수 있음 >> front 로 확인 권장
        if (contentType == null) {
            throw new S3Exception(NOT_IMAGE);
        }

        if (!contentType.startsWith("image")) {
            if (file.getOriginalFilename().endsWith(".jfif")) {
                return;
            }
            throw new S3Exception(NOT_IMAGE);
        }
    }

    public static void validateImageType(String fileName) {

        List<String> imageTypeList = Arrays.asList(
                "jpg", "jpeg", "jfif", "png", "webp", "gif", "bmp", "tiff", "ppm", "pgm", "pbm", "pnm");

        int exWordCount = fileName.lastIndexOf(".");
        if (exWordCount == -1) {
            throw new S3Exception(NOT_ALLOWED_EXTENSION);
        }

        String extension = fileName.substring(exWordCount + 1).toLowerCase();
        if (!imageTypeList.contains(extension)) {
            throw new S3Exception(NOT_ALLOWED_EXTENSION);
        }
    }


    public static String createUserProfileDir(Long userId) {
        return URL_PREFIX + "/"
                + USER_PROFILE_DIR + "/"
                + userId + "/";
    }

    public static String createArtistProfileDir(Long artistId) {
        return URL_PREFIX + "/"
                + ARTIST_PROFILE_DIR + "/"
                + artistId + "/";
    }

    public static String createEntertainmentLogoDir(Long enterId) {
        return URL_PREFIX + "/"
                + ENTER_LOGO_DIR + "/"
                + enterId + "/";
    }

    public static String createArtistGroupDir(Long groupId) {
        return URL_PREFIX + "/"
                + ARTIST_GROUP_DIR + "/"
                + groupId + "/";
    }

    public static String createArtistFeedDir(String artistName, Long artistFeedId) {
        return URL_PREFIX + "/"
                + ARTIST_FEED_DIR + "/"
                + artistName + "/"
                + artistFeedId + "/";
    }

    public static String createCommunityDir(String groupName, Long communityFeedId) {
        return URL_PREFIX + "/"
                + COMMUNITY_DIR + "/"
                + groupName + "/"
                + communityFeedId + "/";
    }

    public static String createProductDir(String artistName, String productType, Long productId) {
        return URL_PREFIX + "/"
                + PRODUCT_DIR + "/"
                + artistName + "/"
                + productType + "/"
                + productId + "/";
    }
}
