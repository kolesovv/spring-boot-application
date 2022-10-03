package com.edu.ulab.app.repository;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertDeleteCount;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertInsertCount;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertUpdateCount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.User;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

/**
 * Tests of the {@link UserRepository} repository.
 */
@SystemJpaTest
@DisplayName("Testing user repository.")
class UserRepositoryTest {

  private Long userId;
  private User existUser;

  @Autowired
  UserRepository userRepository;

  @BeforeEach
  void setUp() {

    SQLStatementCountValidator.reset();
  }

  @BeforeEach
  public void createUser() {

    userId = 1L;
    existUser = User.builder()
        .id(userId)
        .fullName("Antoni Gaudi")
        .age(18)
        .title("Architect")
        .build();
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql",
      "classpath:sql/3_insert_book_data.sql"
  })
  @Test
  void save_correctUserFormIsGiven_userSaved() {

    //GIVEN
    User user = User.builder()
        .fullName("Cay S. Horstmann")
        .age(18)
        .title("Programmer")
        .build();

    //WHEN
    User actual = userRepository.save(user);

    //THEN
    assertEquals(user.getFullName(), actual.getFullName());
    assertEquals(user.getTitle(), actual.getTitle());
    assertEquals(user.getAge(), actual.getAge());

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql"
  })
  @Test
  void update_userExists_userIsUpdated() {

    //GIVEN
    existUser.setTitle("Painter");

    //WHEN
    User actual = userRepository.save(existUser);

    //THEN
    assertEquals(existUser, actual);

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql"
  })
  @Test
  void findById_userExists_userIsReturned() {

    //WHEN
    Optional<User> actual = userRepository.findById(userId);

    //THEN
    User returnedUser = actual.get();

    assertEquals(existUser, returnedUser);

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql"})
  @Test
  void findById_userDoesNotExist_userIsNotPresent() {

    //GIVEN
    Long wrongUserId = -1L;

    //WHEN
    Optional<User> actual = userRepository.findById(wrongUserId);

    //THEN
    assertFalse(actual.isPresent());

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql"
  })
  @Test
  void findAll_userExists_userListIsReturned() {

    //GIVEN
    List<User> users = Collections.singletonList(existUser);

    //WHEN
    List<User> actualUsers = (List<User>) userRepository.findAll();

    //THEN
    assertEquals(users, actualUsers);

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql"})
  @Test
  void findAll_userDoesNotExist_emptyUserListIsReturned() {

    //GIVEN
    List<User> users = Collections.emptyList();

    //WHEN
    List<User> actualUsers = (List<User>) userRepository.findAll();

    //THEN
    assertEquals(users, actualUsers);

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql",
      "classpath:sql/3_insert_book_data.sql"
  })
  @Test
  void deleteById_userExists_userIsDeleted() {

    //WHEN
    userRepository.deleteById(userId);

    //THEN
    Optional<User> actual = userRepository.findById(userId);
    assertFalse(actual.isPresent());

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }
}
