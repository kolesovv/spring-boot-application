package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.User;

public interface UserRepository {

  User createUser(User user);

  User updateUser(User user);

  User getUserById(Long id);

  void deleteUserById(Long id);

  boolean existsById(Long id);
}
