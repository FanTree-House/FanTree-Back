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
        } else {
            throw new S3Exception(UPLOAD_ERROR);
        }

        String fileName = makeFileName(originName, fileDir);

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

            //기존 이미지 삭제 로직(위치는 추후 조정)

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

        String key = getImageKey(fileDir);
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
        } catch (Exception e) {
            throw new S3Exception(DELETE_ERROR);
        }
    }

    //Dir 다건 삭제
    public void deleteFilesInBucket(List<String> filedDirs) {

        for (String fileDir : filedDirs) {
            String key = getImageKey(fileDir);
            try {
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));

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
