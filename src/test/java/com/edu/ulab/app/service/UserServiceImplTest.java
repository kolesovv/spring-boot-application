package com.edu.ulab.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

/**
 * Functional testing {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@DisplayName("Testing user functionality.")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  private Long userId;
  private User user;

  @BeforeEach
  public void createUser() {

    userId = 1L;
    user = User.builder()
        .id(userId)
        .fullName("Antoni Gaudi")
        .age(18)
        .title("Architect")
        .build();
  }

  @Captor
  ArgumentCaptor<User> userArgumentCaptor;

  @InjectMocks
  UserServiceImpl userService;

  @Mock
  UserRepository userRepository;

  private String getUserNotFoundMessage(Long userId){

    return String.format("User with id %d doesn't exist", userId);
  }

  @Test
  void createUser_correctUserFormIsGiven_userCreated() {

    //WHEN
    userService.createUser(user);

    //THEN
    Mockito.verify(userRepository).save(userArgumentCaptor.capture());

    assertEquals(user, userArgumentCaptor.getValue());
  }

  @Test
  void updateUser_userExists_userIsUpdated() {

    //GIVEN
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    User updatedUser = user;
    int newAge = 19;
    updatedUser.setAge(newAge);

    //WHEN
    userService.updateUser(updatedUser);

    //THEN
    Mockito.verify(userRepository).save(userArgumentCaptor.capture());
    assertEquals(updatedUser, userArgumentCaptor.getValue());
  }

  @Test
  void updateUser_userDoesNotExist_userNofFoundExceptionIsThrown() {

    //GIVEN
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

    //WHEN
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> userService.updateUser(user));

    //THEN
    assertEquals(UserNotFoundException.class, thrown.getClass());
    assertEquals(getUserNotFoundMessage(userId), thrown.getMessage());
  }

  @Test
  void updateUser_givenBookIdEqualNull_userNofFoundExceptionIsThrown() {

    //GIVEN
    User userWithWrongId = user;
    userWithWrongId.setId(null);

    //WHEN
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> userService.updateUser(userWithWrongId));

    //THEN
    assertEquals(UserNotFoundException.class, thrown.getClass());
    assertEquals(getUserNotFoundMessage(null), thrown.getMessage());
  }

  @Test
  void getUserById_userIdIsGiven_userIsReturned() {

    //GIVEN
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    //WHEN
    User userReturned = userService.getUserById(userId);

    //THEN
    assertEquals(user, userReturned);
  }

  @Test
  void getUserById_userDoesNotExist_userNofFoundExceptionIsThrown() {

    //GIVEN
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

    //WHEN
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> userService.getUserById(userId));

    //THEN
    assertEquals(UserNotFoundException.class, thrown.getClass());
    assertEquals(getUserNotFoundMessage(userId), thrown.getMessage());
  }

  @Test
  void deleteUserById_userExists_userIsDeleted() {

    //GIVEN
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    //WHEN
    userService.deleteUserById(userId);

    //THEN
    Mockito.verify(userRepository).deleteById(userId);
  }

  @Test
  void deleteUserById_userDoesNotExist_userNofFoundExceptionIsThrown() {

    //GIVEN
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

    //WHEN
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> userService.deleteUserById(userId));

    //THEN
    assertEquals(UserNotFoundException.class, thrown.getClass());
    assertEquals(getUserNotFoundMessage(userId), thrown.getMessage());
  }
}
