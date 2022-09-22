package com.edu.ulab.app.exception;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException(Long userId) {

    super(String.format("%s%d%s", "User with id ", userId, " doesn't exist"));
  }
}
