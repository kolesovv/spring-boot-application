package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

  @Autowired
  private BookRepository bookRepository;

  @Override
  public Book createBook(Book book) {

    log.info("Got book: {}", book);
    Book createdBook = bookRepository.save(book);
    log.info("Created book: {}", createdBook);
    return createdBook;
  }

  @Override
  public Book updateBook(Book book) {

    log.info("Got book: {}", book);
    if (book.getId() != null) {
      Long currentId = book.getId();
      Optional<Book> optionalBook = bookRepository.findById(currentId);

      if (optionalBook.isPresent()) {
        Book updatedBook = bookRepository.save(book);
        log.info("Updated book: {}", updatedBook);
        return updatedBook;
      }
      else {
        throw new BookNotFoundException(currentId);
      }
    }
    else {
      throw new BookNotFoundException(book.getId());
    }
  }

  @Override
  public Book getBookById(Long id) {

    log.info("Got id book: {}", id);
    Optional<Book> optionalBook = bookRepository.findById(id);

    if (optionalBook.isPresent()) {
      Book foundBook = optionalBook.get();
      log.info("Found book: {}", foundBook);
      return foundBook;
    }
    else {
      throw new BookNotFoundException(id);
    }
  }

  @Override
  public void deleteBookById(Long id) {

    log.info("Got id book: {}", id);
    Optional<Book> optionalBook = bookRepository.findById(id);

    if (optionalBook.isPresent()) {
      bookRepository.deleteById(id);
      log.info("Deleted book: {}", optionalBook.get());
    }
    else {
      throw new BookNotFoundException(id);
    }
  }

  @Override
  public void deleteBooksByUserId(Long id) {

    log.info("Got id user: {}", id);
    try {
      bookRepository.deleteAllByUserId(id);
      log.info("Books by user id is deleted");
    }
    catch (NotFoundException ex) {
      throw new NotFoundException("Books for user id " + id + " doesn't exist");
    }
  }

  @Override
  public List<Book> getBookListByUserId(Long id) {

    log.info("Got id user: {}", id);
    List<Book> books = bookRepository.getAllByUserId(id);
    log.info("Got books by user id: {}", books);
    return books;
  }
}
