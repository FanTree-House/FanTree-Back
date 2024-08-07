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
  private final UserRepository userRepository;
  private final JobRepository jobRepository;
  private final EntityManagerFactory entityManagerFactory;
  private final PlatformTransactionManager transactionManager;

  @Bean
  public Job markUsersInactiveJob() {
    return new JobBuilder("InactiveJob", jobRepository)
        .start(markUsersInactiveStep())
        .build();
  }

  @Bean
  public Step markUsersInactiveStep() {
    return new StepBuilder("markUsersInactiveStep", jobRepository)
        .<User, User>chunk(100, transactionManager)
        .reader(inactiveUserReader(null,null))
        .processor(inactiveUserProcessor())
        .writer(inactiveUserWriter())
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<User> inactiveUserReader(
      @Value("#{jobParameters['timeLimitDays']}") Long timeLimitDays,
      @Value("#{jobParameters['currentDate']}") String currentDateStr) {

    if (timeLimitDays == null) {
      timeLimitDays = 3L; // 기본값 3일
    }

    LocalDateTime currentDate = currentDateStr != null ?
        LocalDateTime.parse(currentDateStr) : LocalDateTime.now();

    Map<String, Object> parameterValues = new HashMap<>();
    parameterValues.put("timeLimit", currentDate.minusDays(timeLimitDays));
    parameterValues.put("activeStatus", UserStatusEnum.ACTIVE_USER);

    return new JpaPagingItemReaderBuilder<User>()
        .name("inactiveUserReader")
        .entityManagerFactory(entityManagerFactory)
        .queryString("SELECT u FROM User u WHERE u.lastLoginDate < :timeLimit AND u.status = :activeStatus")
        .parameterValues(parameterValues)
        .pageSize(100)
        .build();
  }

  @Bean
  public ItemProcessor<User, User> inactiveUserProcessor() {
    return user -> {
      user.setInactive();
      return user;
    };
  }

  @Bean
  public JpaItemWriter<User> inactiveUserWriter() {
    JpaItemWriter<User> writer = new JpaItemWriter<>();
    writer.setEntityManagerFactory(entityManagerFactory);
    return writer;
  }
}
