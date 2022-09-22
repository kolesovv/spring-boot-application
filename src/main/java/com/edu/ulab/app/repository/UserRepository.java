package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.User;
import java.util.Optional;

public interface UserRepository {

  User createUser(User user);

  User updateUser(User user);

  Optional<User> getUserById(Long id);

  void deleteUserById(Long id);

  boolean existsById(Long id);
}
