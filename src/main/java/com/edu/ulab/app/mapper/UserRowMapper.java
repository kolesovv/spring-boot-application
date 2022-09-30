package com.edu.ulab.app.mapper;

import com.edu.ulab.app.entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserRowMapper implements RowMapper<User> {

  @Override
  public User mapRow(ResultSet rs, int rowNum) throws SQLException {

    User user = new User();

    user.setId(rs.getLong("id"));
    user.setFullName(rs.getString("full_name"));
    user.setTitle(rs.getString("title"));
    user.setAge(rs.getInt("age"));

    return user;
  }
}
