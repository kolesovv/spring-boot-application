package com.edu.ulab.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDto {

  private Long id;
  private UserDto userDto;
  private String title;
  private String author;
  private long pageCount;
}
