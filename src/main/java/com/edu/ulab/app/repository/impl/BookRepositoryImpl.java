package com.edu.ulab.app.repository.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.storage.Storage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {

  @Autowired
  Storage storage;

  @Override
  public Book createBook(Book book) {

    storage.getStorageOfBooks()
        .put(book.getId(), book);

    return storage.getStorageOfBooks()
        .get(book.getId());
  }

  @Override
  public Book updateBook(Book book) {

    storage.getStorageOfBooks()
        .replace(book.getId(), book);

    return storage.getStorageOfBooks()
        .get(book.getId());
  }

  @Override
  public Optional<Book> getBookById(Long id) {

    Book book = storage.getStorageOfBooks()
        .get(id);

    return Optional.ofNullable(book);
  }

  @Override
  public void deleteBookById(Long id) {

    storage.getStorageOfBooks()
        .remove(id);
  }

  @Override
  public void deleteBooksByUserId(Long id) {

    storage.getStorageOfBooks()
        .entrySet()
        .removeIf(entry -> Objects.equals(entry.getValue()
        .getUserId(), id));
  }

  @Override
  public boolean existById(Long id) {

    return storage.getStorageOfBooks()
        .containsKey(id);
  }

  @Override
  public List<Book> getBookListByUserId(Long id) {

    List<Book> books = new ArrayList<>(storage.getStorageOfBooks()
        .values());

    return books.stream()
        .filter(book -> Objects.equals(book.getUserId(), id))
        .toList();
  }
}
