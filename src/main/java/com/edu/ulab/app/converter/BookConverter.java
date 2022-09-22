package com.edu.ulab.app.converter;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

  public BookDto mapToDto(Book book) {

    return BookDto.builder()
        .id(book.getId())
        .userId(book.getUserId())
        .title(book.getTitle())
        .author(book.getAuthor())
        .pageCount(book.getPageCount())
        .build();
  }

  public Book mapToEntity(BookDto bookDto) {

    return Book.builder()
        .id(bookDto.getId())
        .userId(bookDto.getUserId())
        .title(bookDto.getTitle())
        .author(bookDto.getAuthor())
        .pageCount(bookDto.getPageCount())
        .build();
  }
}
