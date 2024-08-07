//package com.example.fantreehouse;
//
//import com.example.fantreehouse.common.config.InactiveUserJobConfig;
//import com.example.fantreehouse.domain.user.entity.User;
//import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
//import com.example.fantreehouse.domain.user.repository.UserRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Query;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.item.Chunk;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.batch.item.database.JpaPagingItemReader;
//import org.springframework.context.annotation.Import;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.catchThrowable;
//import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@Import(InactiveUserJobConfig.class)
//class BatchTest1 {
//
//  @Mock
//  private UserRepository userRepository;
//
//  @Mock
//  private JobRepository jobRepository;
//
//  @Mock
//  private EntityManagerFactory entityManagerFactory;
//
//  @Mock
//  private PlatformTransactionManager transactionManager;
//
//  private InactiveUserJobConfig jobConfig;
//
//  @BeforeEach
//  void setUp() {
//    MockitoAnnotations.openMocks(this);
//    jobConfig = new InactiveUserJobConfig(userRepository, jobRepository, entityManagerFactory, transactionManager);
//  }
//
//  @Test
//  void testInactiveUserReader() {
//    Long timeLimitDays = 3L;
//    String currentDateStr = LocalDateTime.now().toString();
//
//    JpaPagingItemReader<User> reader = jobConfig.inactiveUserReader(timeLimitDays, currentDateStr);
//
//    assertThat(reader).isNotNull();
//
//    // Mock EntityManager and Query
//    EntityManager entityManager = mock(EntityManager.class);
//    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
//
//    Query query = mock(Query.class);
//    when(entityManager.createQuery(anyString())).thenReturn(query);
//
//    // Validate reader's query string indirectly
//    verify(entityManager).createQuery("SELECT u FROM User u WHERE u.lastLoginDate < :timeLimit AND u.status = :activeStatus");
//  }
//
//  @Test
//  void testInactiveUserProcessorWithException() {
//    User user = new User();
//    user.setStatus(UserStatusEnum.ACTIVE_USER);
//
//    // Custom processor that throws an exception for testing
//    ItemProcessor<User, User> processor = user1 -> {
//      throw new RuntimeException("Processing error");
//    };
//
//    Throwable thrown = catchThrowable(() -> {
//      processor.process(user);
//    });
//
//    assertThat(thrown).isInstanceOf(RuntimeException.class)
//        .hasMessageContaining("Processing error");
//  }
//
////  @Test
////  void testInactiveUserWriter() {
////    JpaItemWriter<User> writer = jobConfig.inactiveUserWriter();
////
////    // Mock EntityManager
////    EntityManager entityManager = mock(EntityManager.class);
////    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
////
////    // Prepare test data
////    User user = new User();
////    List<User> users = List.of(user);
////
////    // Use writer to write the list of users
////    writer.write(users);
////
////    // Verify that the EntityManager's persist method is called for each user
////    verify(entityManager).persist(user);
////  }
//
//  @Test
//  void testMarkUsersInactiveJob() {
//    Job job = jobConfig.markUsersInactiveJob();
//
//    assertThat(job).isNotNull();
//    assertThat(job.getName()).isEqualTo("InactiveJob");
//  }
//
//  @Test
//  void testMarkUsersInactiveStep() {
//    Step step = jobConfig.markUsersInactiveStep();
//
//    assertThat(step).isNotNull();
//    assertThat(step.getName()).isEqualTo("markUsersInactiveStep");
//  }
//}