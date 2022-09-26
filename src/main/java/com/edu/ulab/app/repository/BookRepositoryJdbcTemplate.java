package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookRowMapper;
import com.edu.ulab.app.mapper.UserRowMapper;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryJdbcTemplate {

  @Autowired
  private final JdbcTemplate jdbcTemplate;

  public BookRepositoryJdbcTemplate(JdbcTemplate jdbcTemplate) {

    this.jdbcTemplate = jdbcTemplate;
  }

  public Book save(Book book) {

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(con -> {
      PreparedStatement ps =
          con.prepareStatement("INSERT INTO book (user_id, title, author, page_count) VALUES (?, ?, ?, ?)",
              new String[] {"id"});
      ps.setLong(1, book.getUser().getId());
      ps.setString(2, book.getTitle());
      ps.setString(3, book.getAuthor());
      ps.setLong(4, book.getPageCount());

      return ps;

    }, keyHolder);

    long id = keyHolder.getKey().longValue();

    return findById(id).orElseThrow(() -> new BookNotFoundException(id));
  }

  public Book update(Book book) {

    jdbcTemplate.update("UPDATE book SET user_id=?, title=?, author=?, page_count=? WHERE id=?",
        book.getUser().getId(), book.getTitle(), book.getAuthor(), book.getPageCount(), book.getId());

    return findById(book.getId()).orElseThrow(() -> new BookNotFoundException(book.getId()));
  }

  public Optional<Book> findById(Long id) {

    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(
          "SELECT * FROM book b JOIN person p on p.id = b.user_id WHERE b.id=?",
          new BookRowMapper(new UserRowMapper()), id));
    }
    catch (EmptyResultDataAccessException ex) {
      throw new BookNotFoundException(id);
    }
  }

  public void deleteById(Long id) {

    jdbcTemplate.update("DELETE FROM book b WHERE b.id=?", id);
  }

  public void deleteAllByUserId(Long id) {

    jdbcTemplate.update("DELETE FROM book b WHERE b.user_id=?", id);
  }

  public List<Book> getAllByUserId(Long id) {

    return jdbcTemplate.query("SELECT * FROM book b JOIN person p on p.id = b.user_id WHERE b.user_id=?",
            new BookRowMapper(new UserRowMapper()), id)
        .stream()
        .toList();
  }
}
