package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

  private Long bookId = 0L;

  @Autowired
  private BookRepository bookRepository;

  @Override
  public Book createBook(Book book) {

    Long generatedId = bookId++;
    book.setId(generatedId);
    bookRepository.createBook(book);

    return bookRepository.getBookById(generatedId);
  }

  @Override
  public Book updateBook(Book book) {

    Long currentId = book.getId();

    if (bookRepository.existById(currentId)) {
      bookRepository.updateBook(book);
    }
    else {
      throw new BookNotFoundException(currentId);
    }
    return bookRepository.getBookById(currentId);
  }

  @Override
  public Book getBookById(Long id) {

    if (!bookRepository.existById(id)) {
      throw new BookNotFoundException(id);
    }

    return bookRepository.getBookById(id);
  }

  @Override
  public void deleteBookById(Long id) {

    if (!bookRepository.existById(id)) {
      throw new BookNotFoundException(id);
    }

    bookRepository.deleteBookById(id);
  }

  @Override
  public void deleteBooksByUserId(Long id) {

    try {
      bookRepository.deleteBooksByUserId(id);
    }
    catch (NotFoundException ex) {
      throw new NotFoundException("Books for user id " + id + " doesn't exist");
    }
  }

  @Override
  public List<Book> getBookListByUserId(Long id) {

    return bookRepository.getBookListByUserId(id);
  }
}
