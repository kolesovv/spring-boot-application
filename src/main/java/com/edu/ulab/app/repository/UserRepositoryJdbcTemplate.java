package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.edu.ulab.app.mapper.UserRowMapper;
import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryJdbcTemplate {

  @Autowired
  private final JdbcTemplate jdbcTemplate;

  public UserRepositoryJdbcTemplate(JdbcTemplate jdbcTemplate) {

    this.jdbcTemplate = jdbcTemplate;
  }

  public User save(User user) {

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement("INSERT INTO person(full_name, title, age) VALUES (?, ?, ?)",
          new String[]{"id"});
      ps.setString(1, user.getFullName());
      ps.setString(2, user.getTitle());
      ps.setLong(3, user.getAge());

      return ps;

    }, keyHolder);

    long id = keyHolder.getKey().longValue();

    return findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  public User update (User user) {

    jdbcTemplate.update("UPDATE person SET full_name=?, title=?, age=? WHERE id=?",
        user.getFullName(), user.getTitle(), user.getAge(), user.getId());

    return findById(user.getId()).orElseThrow(() -> new UserNotFoundException(user.getId()));
  }

  public Optional<User> findById(Long id) {

    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM person p WHERE p.id=?",
          new UserRowMapper(), id));
    }
    catch (EmptyResultDataAccessException ex){
      throw new UserNotFoundException(id);
    }
  }

  public void deleteById(Long id) {

    jdbcTemplate.update("DELETE FROM person p WHERE p.id=?", id);
  }
}
