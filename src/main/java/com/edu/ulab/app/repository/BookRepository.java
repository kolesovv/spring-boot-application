package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {

  Book createBook(Book book);

  Book updateBook(Book book);

  Optional<Book> getBookById(Long id);

  void deleteBookById(Long id);

  void deleteBooksByUserId(Long id);

  boolean existById(Long id);

  List<Book> getBookListByUserId(Long id);
}
