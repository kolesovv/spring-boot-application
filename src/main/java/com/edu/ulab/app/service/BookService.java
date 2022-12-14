package com.edu.ulab.app.service;

import com.edu.ulab.app.entity.Book;
import java.util.List;

public interface BookService {

  Book createBook(Book book);

  Book updateBook(Book book);

  Book getBookById(Long id);

  void deleteBookById(Long id);

  void deleteBooksByUserId(Long id);

  List<Book> getBookListByUserId(Long id);
}
