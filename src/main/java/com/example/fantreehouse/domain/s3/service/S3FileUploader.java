package com.example.fantreehouse.domain.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.example.fantreehouse.common.config.S3Config;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class S3FileUploader {

    private final AmazonS3Client amazonS3Client;
    private final String mainUrl = "https://fantree.s3.ap-northeast-2.amazonaws.com/";
    public static final String BASIC_DIR = "https://fantree.s3.ap-northeast-2.amazonaws.com/Fantree/ProfileDefaultImage/";
    public static final String START_PROFILE_URL = "https://fantree.s3.ap-northeast-2.amazonaws.com/Fantree/ProfileDefaultImage/Start.png";
    public static final String DEFAULT_URL = "https://fantree.s3.ap-northeast-2.amazonaws.com/Fantree/ProfileDefaultImage/Default.png";
    private final String USER_PROFILE_URL = "https://fantree.s3.ap-northeast-2.amazonaws.com/Fantree/ProfileDefaultImage/User.png";
    private final String ARTIST_PROFILE_URL = "https://fantree.s3.ap-northeast-2.amazonaws.com/Fantree/ProfileDefaultImage/Artist.png";
    private final String ENTER_PROFILE_URL = "https://fantree.s3.ap-northeast-2.amazonaws.com/Fantree/ProfileDefaultImage/Entertainment.png";
    private final String ARTIST_GROUP_PROFILE_URL = "https://fantree.s3.ap-northeast-2.amazonaws.com/Fantree/ProfileDefaultImage/ArtistGroup.png";
    private final String ADMIN_PROFILE_URL = "https://fantree.s3.ap-northeast-2.amazonaws.com/Fantree/ProfileDefaultImage/Admin.png";


    @Value("${cloud.aws.s3.bucket}")
    private final String bucket = "fantree";

    private static final String NO_IMAGE = "업로드된 이미지가 없습니다.";

    public String saveProfileImage(MultipartFile file, Long id, UserRoleEnum userRoleEnum) {
        if (!isFileExists(file)) {
            return saveBasicProfileIMG(userRoleEnum);
        }

        String uploadFileName = file.getOriginalFilename();
        if (uploadFileName == null || !uploadFileName.contains(".")) {
            throw new S3Exception(UPLOAD_ERROR);
        }

        validateImageTypeWithContentType(file);
        validateImageType(uploadFileName);

        String originName = uploadFileName.replace(" ", "");

        //저장 경로 설정
        String fileDir;
        if (userRoleEnum.equals(UserRoleEnum.USER)) {
            fileDir = createUserProfileDir(id);
        } else if (userRoleEnum.equals(UserRoleEnum.ARTIST)) {
            fileDir = createArtistProfileDir(id);
        } else if (userRoleEnum.equals(UserRoleEnum.ENTERTAINMENT)) {
            fileDir = createEntertainmentLogoDir(id);
        } else if (userRoleEnum.equals(UserRoleEnum.ADMIN)) {
            fileDir = createAdminProfileDir(id);
        } else {
            throw new S3Exception(UPLOAD_ERROR);
        }

        String fileName = makeFileName(originName, fileDir);

        return uploadFileToS3(file, fileName);

    }

    private String saveBasicProfileIMG(UserRoleEnum userRoleEnum) {
        if (userRoleEnum.equals(UserRoleEnum.USER)) {
            return USER_PROFILE_URL;
        } else if (userRoleEnum.equals(UserRoleEnum.ARTIST)) {
            return ARTIST_PROFILE_URL;
        } else if (userRoleEnum.equals(UserRoleEnum.ENTERTAINMENT)) {
            return ENTER_PROFILE_URL;
        } else if (userRoleEnum.equals(UserRoleEnum.ADMIN)) {
            return ADMIN_PROFILE_URL;
        } else {
            throw new S3Exception(UPLOAD_ERROR);
        }
    }

    public String saveArtistGroupImage(MultipartFile file, Long artistGroupId) {
        if (!isFileExists(file)) {
            return ARTIST_GROUP_PROFILE_URL;
        }

        String uploadFileName = file.getOriginalFilename();
        if (uploadFileName == null || !uploadFileName.contains(".")) {
            throw new S3Exception(UPLOAD_ERROR);
        }

        validateImageTypeWithContentType(file);
        validateImageType(uploadFileName);

        String originName = uploadFileName.replace(" ", "");

        //저장 경로 설정
        String fileDir = createArtistGroupDir(artistGroupId);
        String fileName = makeFileName(originName, fileDir);

        return uploadFileToS3(file, fileName);
    }

    public String saveArtistFeedImage(MultipartFile file, String artistName, Long feedId) {
        if (!isFileExists(file)) {
            return NO_IMAGE;
        }

        String uploadFileName = file.getOriginalFilename();
        if (uploadFileName == null || !uploadFileName.contains(".")) {
            throw new S3Exception(UPLOAD_ERROR);
        }

        validateImageTypeWithContentType(file);
        validateImageType(uploadFileName);

        String originName = uploadFileName.replace(" ", "");

        //저장 경로 설정
        String fileDir = createArtistFeedDir(artistName, feedId);
        String fileName = makeFileName(originName, fileDir);

        return uploadFileToS3(file, fileName);
    }

    public String saveCommunityImage(MultipartFile file, String groupName, Long communityFeedId) {
        if (!isFileExists(file)) {
            return NO_IMAGE;
        }

        String uploadFileName = file.getOriginalFilename();
        if (uploadFileName == null || !uploadFileName.contains(".")) {
            throw new S3Exception(UPLOAD_ERROR);
        }

        validateImageTypeWithContentType(file);
        validateImageType(uploadFileName);

        String originName = uploadFileName.replace(" ", "");

        //저장 경로 설정
        String fileDir = createCommunityDir(groupName, communityFeedId);
        String fileName = makeFileName(originName, fileDir);

        return uploadFileToS3(file, fileName);

    }

    public String saveProductImage(MultipartFile file, String artistName, String productType, Long productId) {

        if (!isFileExists(file)) {
            return NO_IMAGE;
        }

        String uploadFileName = file.getOriginalFilename();
        if (uploadFileName == null || !uploadFileName.contains(".")) {
            throw new S3Exception(UPLOAD_ERROR);
        }

        validateImageTypeWithContentType(file);
        validateImageType(uploadFileName);

        String originName = uploadFileName.replace(" ", "");

        //저장 경로 설정
        String fileDir = createProductDir(artistName, productType, productId);
        String fileName = makeFileName(originName, fileDir);

        return uploadFileToS3(file, fileName);
    }


    private String uploadFileToS3(MultipartFile file, String fileName) {

        ObjectMetadata metadata = new ObjectMetadata(); //메타데이터
        metadata.setContentType(file.getContentType()); //이미지만 받을 예정이라면 image 라고 명시해도 좋음

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException e) {
            log.error("IOException 발생");
            e.printStackTrace();

        } catch (Exception e) {
            throw new S3Exception(UPLOAD_ERROR);
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //단건 조회
    public String getFileUrl(String imageUrl) {
        return amazonS3Client.getUrl(bucket, imageUrl).toString();
    }

    //Dir 단건 삭제
    public void deleteFileInBucket(String fileDir) {

        if (fileDir.startsWith(BASIC_DIR)) {
            return;
        }

        String key = getImageKey(fileDir);
        try {
            if (amazonS3Client.doesObjectExist(bucket, key)) {
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
            } else {
                throw new NotFoundException(NOT_STORED_FILE_NAME);//테이블에 저장되어있는 실체없는 url 을 삭제하라고 알려주기
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new S3Exception(DELETE_ERROR);
        }

    }

    //Dir 다건 삭제
    public void deleteFilesInBucket(List<String> filedDirs) {

        for (String fileDir : filedDirs) {

            String key = getImageKey(fileDir);
            try {
                if (amazonS3Client.doesObjectExist(bucket, key)) {
                    amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
                } else {
                    throw new NotFoundException(NOT_STORED_FILE_NAME);
                }
            } catch (NotFoundException e) {
                throw e;
            } catch (Exception e) {
                throw new S3Exception(DELETE_ERROR);
            }
        }
    }

    //파일이름 생성
    private String makeFileName(String originName, String fileDir) {
        UUID uuid = UUID.randomUUID();
        return fileDir + originName + "_" + uuid;
    }

    //Url 잘라내기(파일명 추출)
    private String getImageKey(String fileDir) {
        String key = fileDir.replace(mainUrl, "");
        return URLDecoder.decode(key, StandardCharsets.UTF_8);
    }
}
