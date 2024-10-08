package com.example.fantreehouse.domain.user.service;

import static com.example.fantreehouse.common.enums.ErrorType.USER_NOT_FOUND;

import com.example.fantreehouse.auth.RedisUtil;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.domain.user.dto.EmailCheckRequestDto;
import com.example.fantreehouse.domain.user.dto.EmailRequestDto;
import com.example.fantreehouse.domain.user.entity.MailAuth;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSendService {


  private final JavaMailSender mailSender;
  private final RedisUtil redisUtil;
  private int authNumber;
  private final UserRepository userRepository;

  @Transactional
  public boolean CheckAuthNum(String loginId, EmailCheckRequestDto requestDto) {
    MailAuth mailAuth  = redisUtil.getData(loginId);

    if(!mailAuth.getAuthNum().equals(requestDto.getAuthNum())) {
      return false;
    }

    mailAuth.validEmail();
    redisUtil.setData(loginId, mailAuth);
    return true;
  }

  //임의의 6자리 양수를 반환합니다.
  public int makeRandomNumber() {
    Random r = new Random();
    String randomNumber = "";
    for (int i = 0; i < 6; i++) {
      randomNumber += Integer.toString(r.nextInt(10));
    }
    authNumber = Integer.parseInt(randomNumber);
    return authNumber;
  }

  //mail을 어디서 보내는지, 어디로 보내는지 , 인증 번호를 html 형식으로 어떻게 보내는지 작성합니다.
  public String joinEmail(EmailRequestDto requestDto) {
    String randomNumber = String.valueOf(makeRandomNumber());
    String loginId = requestDto.getLoginId();
    // 환경변수 설정
    String setFrom = "fantreecompany@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력
    String toMail = requestDto.getEmail();
    String title = "회원 가입 인증 이메일 입니다."; // 이메일 제목
    MailAuth mailAuth = new MailAuth(loginId,toMail,randomNumber);
    String content =
        "FANTREE 가입을 위해 인증번호가 필요합니다.." +    //html 형식으로 작성 !
            "<br><br>" +
            "인증 번호는 " + authNumber + "입니다." +
            "<br>" +
            "인증번호를 제대로 입력해주세요"; //이메일 내용 삽입
    mailSend(setFrom, toMail, title, content, mailAuth);
    redisUtil.setData(loginId, mailAuth);
    return Integer.toString(authNumber);
  }

  public String changeInactiveUserStatusEmail(EmailRequestDto requestDto){
    String randomNumber = String.valueOf(makeRandomNumber());

    String loginId = requestDto.getLoginId();
    UserStatusEnum status = UserStatusEnum.INACTIVE_USER;
    User inactiveUser = userRepository.findByLoginIdAndEmailAndStatus(loginId,
        requestDto.getEmail(), status).orElseThrow(()->  new NotFoundException(USER_NOT_FOUND));

    String setFrom = "fantreecompany@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력
    String toMail = requestDto.getEmail();
    String title = "휴면 계정 해제 이메일입니다."; // 이메일 제목
    MailAuth mailAuth = new MailAuth(inactiveUser.getLoginId(),
        inactiveUser.getEmail(),randomNumber,inactiveUser.getStatus());
    String content =
        "휴면 계정 해제를 위해 인증번호가 필요합니다." +    //html 형식으로 작성 !
            "<br><br>" +
            "인증 번호는 " + authNumber + "입니다." +
            "<br>" +
            "인증번호를 제대로 입력해주세요"; //이메일 내용 삽입
    mailSend(setFrom, toMail, title, content, mailAuth);
    redisUtil.setData(loginId, mailAuth);
    return Integer.toString(authNumber);
  }



  //이메일을 전송합니다.
  public void mailSend(String setFrom, String toMail, String title, String content, MailAuth mailAuth) {
    MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
      // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
      helper.setFrom(setFrom);//이메일의 발신자 주소 설정
      helper.setTo(toMail);//이메일의 수신자 주소 설정
      helper.setSubject(title);//이메일의 제목을 설정
      helper.setText(content, true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
      mailSender.send(message);
    } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
      // 이러한 경우 MessagingException이 발생
      e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
    }
    redisUtil.setDataExpire(mailAuth.getLoginId(), mailAuth, 60 * 5L);
  }
}
