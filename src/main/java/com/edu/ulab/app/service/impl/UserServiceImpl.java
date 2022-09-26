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

  @Autowired
  private UserRepository userRepository;

  @Override
  public User createUser(User user) {

    log.info("Got user: {}", user);
    User createdUser = userRepository.save(user);
    log.info("Created user: {}", createdUser);
    return createdUser;
  }

  @Override
  public User updateUser(User user) {

    log.info("Got user: {}", user);
    if (user.getId() != null) {
      Long currentId = user.getId();
      Optional<User> optionalUser = userRepository.findById(currentId);

      if (optionalUser.isPresent()) {
        User updatedUser = userRepository.save(user);
        log.info("Updated user: {}", updatedUser);
        return updatedUser;
      }
      else {
        throw new UserNotFoundException(currentId);
      }
    }
    else {
      throw new UserNotFoundException(user.getId());
    }
  }

  @Override
  public User getUserById(Long id) {

    log.info("Got id user: {}", id);
    Optional<User> optionalUser = userRepository.findById(id);

    if (optionalUser.isPresent()) {
      User foundUser = optionalUser.get();
      log.info("Found user: {}", foundUser);
      return foundUser;
    }
    else {
      throw new UserNotFoundException(id);
    }
  }

  @Override
  public void deleteUserById(Long id) {

    log.info("Got id user: {}", id);
    Optional<User> optionalUser = userRepository.findById(id);

    if (optionalUser.isPresent()) {
      userRepository.deleteById(id);
      log.info("Deleted user: {}", optionalUser.get());
    }
    else {
      throw new UserNotFoundException(id);
    }
  }
}
