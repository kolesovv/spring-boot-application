package com.edu.ulab.app.mapper;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BookRowMapper implements RowMapper<Book> {

  @Autowired
  private final UserRowMapper userRowMapper;

  public BookRowMapper(UserRowMapper userRowMapper) {

    this.userRowMapper = userRowMapper;
  }

  @Override
  public Book mapRow(ResultSet rs, int rowNum) throws SQLException {

    Book book = new Book();
    User user = userRowMapper.mapRow(rs, rowNum);

    book.setId(rs.getLong("id"));
    book.setUser(user);
    book.setTitle(rs.getString("title"));
    book.setAuthor(rs.getString("author"));
    book.setPageCount(rs.getLong("page_count"));

    return book;
  }
}
