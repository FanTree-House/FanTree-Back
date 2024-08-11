package com.example.fantreehouse.common.schedule;

import com.example.fantreehouse.common.config.InactiveUserJobConfig;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchScheduler {
  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private InactiveUserJobConfig inactiveUserJobConfig;

  @Scheduled(cron = "* 0 0 * * *") // 자정마다
  public void runJob() throws Exception {
    JobParameters parameters = new JobParametersBuilder()
        .addString("markUsersInactiveJob", "inactiveJob" + LocalDateTime.now())
        .toJobParameters();
    // add parameters as needed
    log.debug("스케쥴러 확인");
    jobLauncher.run(inactiveUserJobConfig.markUsersInactiveJob(), parameters);
  }
}
