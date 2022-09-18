package com.edu.ulab.app.repository.impl;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

  @Autowired
  Storage storage;

  @Override
  public User createUser(User user) {

    storage.getStorageOfUsers()
        .put(user.getId(), user);

    return storage.getStorageOfUsers()
        .get(user.getId());
  }

  @Override
  public User updateUser(User user) {

    storage.getStorageOfUsers()
        .replace(user.getId(), user);

    return storage.getStorageOfUsers()
        .get(user.getId());
  }

  @Override
  public User getUserById(Long id) {

    return storage.getStorageOfUsers()
        .get(id);
  }

  @Override
  public void deleteUserById(Long id) {

    storage.getStorageOfUsers()
        .remove(id);
  }

  @Override
  public boolean existsById(Long id) {

    return storage.getStorageOfUsers()
        .containsKey(id);
  }
}
