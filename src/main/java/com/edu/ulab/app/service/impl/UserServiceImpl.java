package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
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
    userRepository.createUser(user);
    return userRepository.getUserById(generatedId);
  }

  @Override
  public User updateUser(User user) {

    Long currentId = user.getId();

    if (userRepository.existsById(currentId)) {
      userRepository.updateUser(user);
    }
    else {
      throw new UserNotFoundException(currentId);
    }

    return userRepository.getUserById(user.getId());
  }

  @Override
  public User getUserById(Long id) {

    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException(id);
    }

    return userRepository.getUserById(id);
  }

  @Override
  public void deleteUserById(Long id) {

    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException(id);
    }
    else {
      userRepository.deleteUserById(id);
    }
  }
}
