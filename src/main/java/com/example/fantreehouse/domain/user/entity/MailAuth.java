package com.example.fantreehouse.domain.user.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MailAuth {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String loginId;

  private String email;

  private String authNum;

  @Enumerated(EnumType.STRING)
  private UserStatusEnum status;

  public MailAuth(String loginId, String email, String authNum, UserStatusEnum status){
    this.loginId = loginId;
    this.email = email;
    this.authNum = authNum;
    this.status = UserStatusEnum.NON_AUTH_USER;
  }

  public void validEmail(){
    this.status = UserStatusEnum.ACTIVE_USER;
  }


}
