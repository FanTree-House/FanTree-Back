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

    @Value("${cloud.aws.s3.bucket}")
    private final String bucket = "fantree";

    private static final String NO_IMAGE = "업로드된 이미지가 없습니다.";

    public String saveProfileImage(MultipartFile file, Long id, UserRoleEnum userRoleEnum) {
        if (!isFileExists(file)) {
            return NO_IMAGE;
        }

        String uploadFileName = file.getOriginalFilename();
        if (uploadFileName == null || !uploadFileName.contains(".")) {
            throw new S3Exception(UPLOAD_ERROR);
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new S3Exception(NOT_IMAGE);
        }

        validateImageTypeWithContentType(contentType);
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
        } else {
            throw new S3Exception(UPLOAD_ERROR);
        }

        UUID uuid = UUID.randomUUID();
        String fileName = fileDir + originName + "_" + uuid;

        return uploadFileToS3(file, fileName);

    }

    public String saveArtistGroupImage(MultipartFile file, Long artistGroupId) {
        if (!isFileExists(file)) {
            return NO_IMAGE;
        }

        String uploadFileName = file.getOriginalFilename();
        if (uploadFileName == null || !uploadFileName.contains(".")) {
            throw new S3Exception(UPLOAD_ERROR);
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new S3Exception(NOT_IMAGE);
        }

        validateImageTypeWithContentType(contentType);
        validateImageType(uploadFileName);

        String originName = uploadFileName.replace(" ", "");

        //저장 경로 설정
        String fileDir = createArtistGroupDir(artistGroupId);

        UUID uuid = UUID.randomUUID(); // 중복되지 않는 문자열 생성
        String fileName = fileDir + originName + "_" + uuid; // 업로드할 이미지 이름;

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

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new S3Exception(NOT_IMAGE);
        }

        validateImageTypeWithContentType(contentType);
        validateImageType(uploadFileName);

        String originName = uploadFileName.replace(" ", "");

        //저장 경로 설정
        String fileDir = createArtistFeedDir(artistName, feedId);

        UUID uuid = UUID.randomUUID(); // 중복되지 않는 문자열 생성
        String fileName = fileDir + originName + "_" + uuid; // 업로드할 이미지 이름;


        return uploadFileToS3(file, fileName);
    }

    public String saveEnterFeedImage(MultipartFile file, String enterName, String feedCategory, Long enterFeedId) {
        if (!S3FileUploaderUtil.isFileExists(file)) {
            return NO_IMAGE;
        }

        String uploadFileName = file.getOriginalFilename();
        if (uploadFileName == null || !uploadFileName.contains(".")) {
            throw new S3Exception(UPLOAD_ERROR);
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new S3Exception(NOT_IMAGE);
        }

        S3FileUploaderUtil.validateImageTypeWithContentType(contentType);
        S3FileUploaderUtil.validateImageType(uploadFileName);

        String originName = uploadFileName.replace(" ", "");

        //저장 경로 설정
        String fileDir = S3FileUploaderUtil.createEnterFeedDir(
                enterName, feedCategory, enterFeedId);

        UUID uuid = UUID.randomUUID(); // 중복되지 않는 문자열 생성
        String fileName = fileDir + originName + "_" + uuid; // 업로드할 이미지 이름;

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

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new S3Exception(NOT_IMAGE);
        }

        validateImageTypeWithContentType(contentType);
        validateImageType(uploadFileName);

        String originName = uploadFileName.replace(" ", "");

        //저장 경로 설정
        String fileDir = createCommunityDir(groupName, communityFeedId);

        UUID uuid = UUID.randomUUID();
        String fileName = fileDir + originName + "_" + uuid;

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

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new S3Exception(NOT_IMAGE);
        }

        validateImageTypeWithContentType(contentType);
        validateImageType(uploadFileName);

        String originName = uploadFileName.replace(" ", "");

        //저장 경로 설정
        String fileDir = createProductDir(artistName, productType, productId);

        UUID uuid = UUID.randomUUID(); // 중복되지 않는 문자열 생성
        String fileName = fileDir + originName + "_" + uuid; // 업로드할 이미지 이름;

        return uploadFileToS3(file, fileName);
    }

    private String uploadFileToS3(MultipartFile file, String fileName) {

        ObjectMetadata metadata = new ObjectMetadata(); //메타데이터
        metadata.setContentType(file.getContentType()); //이미지만 받을 예정이라면 image 라고 명시해도 좋음

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException e) {
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
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileDir));
        } catch (Exception e) {
            throw new S3Exception(DELETE_ERROR);
        }
    }

    //Dir 다건 삭제
    public void deleteFilesInBucket(List<String> filedDirs) {

        for (String fileDir : filedDirs) {
            try {
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileDir));

            } catch (Exception e) {
                throw new S3Exception(DELETE_ERROR);
            }
        }
    }
}
