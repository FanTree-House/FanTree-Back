//package com.example.fantreehouse;
//
//import com.example.fantreehouse.common.config.InactiveUserJobConfig;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//import com.example.fantreehouse.domain.user.entity.User;
//import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
//import com.example.fantreehouse.domain.user.repository.UserRepository;
//import jakarta.persistence.EntityManagerFactory;
//import java.time.LocalDateTime;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.core.BatchStatus;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.test.JobLauncherTestUtils;
//import org.springframework.batch.test.context.SpringBatchTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.PlatformTransactionManager;
//
//
//@Configuration
//@ExtendWith(SpringExtension.class)
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//@SpringBatchTest
//@Import(InactiveUserJobConfig.class) // 필요한 구성 클래스를 명시
//class InactiveUserJobConfigTest {
//
//  @Autowired
//  private JobLauncherTestUtils jobLauncherTestUtils;
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @BeforeEach
//  void setUp() {
//    // 데이터베이스에 초기 데이터 삽입
//    User inactiveUser = new User();
//    inactiveUser.setLastLoginDate(LocalDateTime.now().minusDays(5));
//    inactiveUser.setStatus(UserStatusEnum.ACTIVE_USER);
//    userRepository.save(inactiveUser);
//  }
//
//  @Test
//  void testMarkUsersInactiveJob() throws Exception {
//    JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
//    jobParametersBuilder.addLong("timeLimitDays", 3L);
//    jobParametersBuilder.addString("currentDate", LocalDateTime.now().toString());
//
//    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParametersBuilder.toJobParameters());
//
//    assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
//
//    User updatedUser = userRepository.findAll().get(0);
//    assertThat(updatedUser.getStatus()).isEqualTo(UserStatusEnum.INACTIVE_USER);
//  }
//}
