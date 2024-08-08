package com.example.fantreehouse.domain.user.dto;

import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class SignUpRequestDto {

  @Size(min = 4, max = 10)
  @Pattern(regexp = "^[a-z0-9]+$", message = "영어 소문자와 숫자만 입력 가능합니다.")
  private String id;

  private String name;

  private String nickname;

  @Email
  @NotEmpty(message = "이메일을 입력해 주세요")
  private String email;

  @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{}|;:'\",.<>?/]+$", message = "비밀번호는 알파벳 대소문자, 숫자 및 특수문자만 포함할 수 있습니다.")
  @Size(min = 8, max = 15)
  private String password;

  private MultipartFile file;

  private String checkPassword;

//  UserRoleEnum roleName;
//  String token;

  private boolean admin = false;
  private String adminToken = "";

  private boolean artist = false;
  private String artistToken = "";

  private boolean entertainment = false;
  private String entertainmentToken = "";

  @JsonCreator
  public SignUpRequestDto(String id, String name, String nickname, String email, String password,
      MultipartFile file, String checkPassword, String adminToken, String artistToken, String entertainmentToken) {
    this.id = id;
    this.name = name;
    this.nickname = nickname;
    this.email = email;
    this.password = password;
    this.file = file;
    this.checkPassword = checkPassword;
    this.adminToken = adminToken;
    this.artistToken = artistToken;
    this.entertainmentToken = entertainmentToken;
    setAdmin(adminToken);
    setArtist(artistToken);
    setEnter(entertainmentToken);
  }

  private void setAdmin(String adminToken){
    if(StringUtils.hasLength(adminToken)){
      this.admin = true;
    }
    else this.admin = false;
  }
  private void setEnter(String entertainmentToken){
    if(StringUtils.hasLength(entertainmentToken)){
      this.entertainment = true;
    }
    else this.entertainment = false;
  }
  private void setArtist(String artistToken){
    if(StringUtils.hasLength(artistToken)){
      this.artist = true;
    }
    else this.artist = false;
  }

}