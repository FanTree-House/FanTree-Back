package com.example.fantreehouse.common.config;

import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class InactiveUserJobConfig {

  private final JobRepository jobRepository;
  private final EntityManagerFactory entityManagerFactory;
  private final PlatformTransactionManager transactionManager;

  // 비활성 사용자를 처리하는 Job을 정의
  @Bean
  public Job markUsersInactiveJob() {
    return new JobBuilder("InactiveJob", jobRepository)
        .start(markUsersInactiveStep())
        .build();
  }

  // Job에서 실행할 Step을 정의
  @Bean
  public Step markUsersInactiveStep() {
    return new StepBuilder("markUsersInactiveStep", jobRepository)
        .<User, User>chunk(100, transactionManager)  // 청크 크기를 100으로 설정
        .reader(inactiveUserReader(null,null))  // 비활성 사용자를 읽는 reader
        .processor(inactiveUserProcessor())  // 사용자 상태를 변경하는 processor
        .writer(inactiveUserWriter())  // 변경된 사용자 정보를 저장하는 writer
        .build();
  }

  // 비활성 사용자를 조회하는 reader를 정의
  @Bean
  @StepScope
  public JpaPagingItemReader<User> inactiveUserReader(
      @Value("#{jobParameters['timeLimitDays']}") Long timeLimitDays,
      @Value("#{jobParameters['currentDate']}") String currentDateStr) {

    // 기본 비활성 기준 일수 설정 (3일)
    if (timeLimitDays == null) {
      timeLimitDays = 3L;
    }

    // 현재 날짜 설정 (파라미터로 받거나 현재 시간 사용)
    LocalDateTime currentDate = currentDateStr != null ?
        LocalDateTime.parse(currentDateStr) : LocalDateTime.now();

    // 쿼리 파라미터 설정
    Map<String, Object> parameterValues = new HashMap<>();
    parameterValues.put("timeLimit", currentDate.minusDays(timeLimitDays));
    parameterValues.put("activeStatus", UserStatusEnum.ACTIVE_USER);

    // JPA를 사용하여 비활성 사용자를 페이징 방식으로 조회하는 reader 생성
    return new JpaPagingItemReaderBuilder<User>()
        .name("inactiveUserReader")
        .entityManagerFactory(entityManagerFactory)
        .queryString("SELECT u FROM User u WHERE u.lastLoginDate < :timeLimit AND u.status = :activeStatus")
        .parameterValues(parameterValues)
        .pageSize(100)
        .build();
  }

  // 사용자 상태를 비활성으로 변경하는 processor 정의
  @Bean
  public ItemProcessor<User, User> inactiveUserProcessor() {
    return user -> {
      user.setInactive(); // 사용자 상태를 비활성으로 설정
      return user;
    };
  }

  // 변경된 사용자 정보를 데이터베이스에 저장하는 writer 정의
  @Bean
  public JpaItemWriter<User> inactiveUserWriter() {
    JpaItemWriter<User> writer = new JpaItemWriter<>();
    writer.setEntityManagerFactory(entityManagerFactory);
    return writer;
  }
}
