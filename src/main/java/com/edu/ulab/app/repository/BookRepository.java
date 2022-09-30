package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Book;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

  @Transactional
  @Query("DELETE FROM book b WHERE b.user_id:=id")
  void deleteAllByUserId(Long id);

  @Transactional
  @Query("SELECT * FROM book b WHERE user_id:=id")
  List<Book> getAllByUserId(Long id);
}
