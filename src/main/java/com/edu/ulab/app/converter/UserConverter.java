package com.edu.ulab.app.converter;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

  public UserDto mapToDto(User user) {

    return UserDto.builder()
        .id(user.getId())
        .fullName(user.getFullName())
        .title(user.getTitle())
        .age(user.getAge())
        .build();
  }

  public User mapToEntity(UserDto userDto) {

    return User.builder()
        .id(userDto.getId())
        .fullName(userDto.getFullName())
        .title(userDto.getTitle())
        .age(userDto.getAge())
        .build();
  }
}
