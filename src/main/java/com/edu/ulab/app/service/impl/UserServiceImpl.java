package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private Long userId = 0L;

  @Autowired
  private UserRepository userRepository;

  @Override
  public User createUser(User user) {

    Long generatedId = userId++;
    user.setId(generatedId);
    return userRepository.createUser(user);
  }

  @Override
  public User updateUser(User user) {

    Long currentId = user.getId();
    Optional<User> optionalUser = userRepository.getUserById(currentId);

    if (optionalUser.isPresent()) {
      return userRepository.updateUser(user);
    }
    else {
      throw new UserNotFoundException(currentId);
    }
  }

  @Override
  public User getUserById(Long id) {

    Optional<User> optionalUser = userRepository.getUserById(id);

    if (optionalUser.isPresent()) {
      return optionalUser.get();
    }
    else {
      throw new UserNotFoundException(id);
    }
  }

  @Override
  public void deleteUserById(Long id) {

    Optional<User> optionalUser = userRepository.getUserById(id);

    if (optionalUser.isPresent()) {
      userRepository.deleteUserById(id);
    }
    else {
      throw new UserNotFoundException(id);
    }
  }
}
