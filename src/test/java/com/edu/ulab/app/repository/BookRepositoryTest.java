package com.edu.ulab.app.repository;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertDeleteCount;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertInsertCount;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertUpdateCount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

@SystemJpaTest
@DisplayName("Testing book repository.")
class BookRepositoryTest {

  @Autowired
  BookRepository bookRepository;

  private Long bookId;
  private Long userId;
  private Book existBook;
  private User existUser;

  public void createUser() {

    userId = 1L;
    existUser = User.builder()
        .id(userId)
        .fullName("Antoni Gaudi")
        .age(18)
        .title("Architect")
        .build();
  }

  @BeforeEach
  public void createBook() {

    createUser();
    bookId = 1L;
    existBook = Book.builder()
        .id(bookId)
        .title("Core Java Volume I Fundamentals")
        .author("Cay S. Horstmann")
        .user(existUser)
        .pageCount(928L)
        .build();
  }

  @BeforeEach
  void setUp() {

    SQLStatementCountValidator.reset();
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql",
      "classpath:sql/3_insert_book_data.sql"
  })
  @Test
  void save_correctBookFormIsGiven_bookSaved(){

    //GIVEN
    Book book = Book.builder()
        .id(1L)
        .title("Python for Everyone")
        .author("Cay S. Horstmann")
        .user(existUser)
        .pageCount(752L)
        .build();

    //WHEN
    Book actual = bookRepository.save(book);

    //THEN
    assertEquals(book.getUser().getId(), actual.getUser().getId());
    assertEquals(book.getTitle(), actual.getTitle());
    assertEquals(book.getAuthor(), actual.getAuthor());
    assertEquals(book.getPageCount(), actual.getPageCount());

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);

  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql",
      "classpath:sql/3_insert_book_data.sql"
  })
  @Test
  void update_bookExists_bookIsUpdated(){

    //GIVEN
    existBook.setTitle("Python for Everyone");
    existBook.setPageCount(752L);

    //WHEN
    Book actual = bookRepository.save(existBook);

    //THEN
    assertEquals(existBook, actual);

    assertSelectCount(2);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql",
      "classpath:sql/3_insert_book_data.sql"
  })
  @Test
  void findById_bookExists_bookIsReturned() {

    //WHEN
    Optional<Book> actual = bookRepository.findById(bookId);

    //THEN
    Book returnedBook = actual.get();

    assertEquals(existBook, returnedBook);

    assertSelectCount(2);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql"})
  @Test
  void findById_bookDoesNotExist_bookIsNotPresent() {

    //WHEN
    Optional<Book> actual = bookRepository.findById(bookId);

    //THEN
    assertFalse(actual.isPresent());

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql",
      "classpath:sql/3_insert_book_data.sql"
  })
  @Test
  void findAll_bookExists_bookListIsReturned() {

    //GIVEN
    Book anotherExistBook = Book.builder()
        .id(2L)
        .title("Core Java Volume II Advanced Features")
        .author("Cay S. Horstmann")
        .user(existUser)
        .pageCount(928L)
        .build();

    List<Book> books = List.of(existBook, anotherExistBook);

    //WHEN
    List<Book> actualBooks = (List<Book>) bookRepository.findAll();

    //THEN
    assertEquals(books, actualBooks);

    assertSelectCount(2);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql"})
  @Test
  void findAll_bookDoesNotExist_emptyBookListIsReturned() {

    //GIVEN
    List<Book> books = Collections.emptyList();

    //WHEN
    List<Book> actualBooks = (List<Book>) bookRepository.findAll();

    //THEN
    assertEquals(books, actualBooks);

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql",
      "classpath:sql/3_insert_book_data.sql"
  })
  @Test
  void deleteById_bookExists_bookIsDeleted() {

    //WHEN
    bookRepository.deleteById(bookId);

    //THEN
    Optional<Book> actual = bookRepository.findById(bookId);
    assertFalse(actual.isPresent());

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql",
      "classpath:sql/3_insert_book_data.sql"
  })
  @Test
  void deleteAllByUserId_userIdIsGiven_booksAreDeleted() {

    //WHEN
    bookRepository.deleteAllByUserId(userId);

    //THEN
    List<Book> books = bookRepository.getAllByUserId(userId);
    assertEquals(0, books.size());

    assertSelectCount(2);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(2);
  }

  @Rollback
  @Sql({"classpath:sql/1_clear_schema.sql",
      "classpath:sql/2_insert_person_data.sql",
      "classpath:sql/3_insert_book_data.sql"
  })
  @Test
  void getAllByUserId_userIdIsGiven_bookListByUserIdIsReturned() {

    //GIVEN
    Long expectedSize = 2L;

    //WHEN
    List<Book> actual = bookRepository.getAllByUserId(userId);

    //THEN
    assertEquals(expectedSize, actual.size());

    assertSelectCount(1);
    assertInsertCount(0);
    assertUpdateCount(0);
    assertDeleteCount(0);
  }
}
