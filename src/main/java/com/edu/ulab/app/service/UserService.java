package com.edu.ulab.app.service;

import com.edu.ulab.app.entity.User;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    void deleteUserById(Long id);
}
