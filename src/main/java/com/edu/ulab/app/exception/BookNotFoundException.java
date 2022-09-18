package com.edu.ulab.app.exception;

public class BookNotFoundException extends NotFoundException {

  public BookNotFoundException(Long bookId) {

    super(String.format("%s%d%s", "Book with id ", bookId, " doesn't exist"));
  }
}
