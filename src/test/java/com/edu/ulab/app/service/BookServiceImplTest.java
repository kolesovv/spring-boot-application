package com.edu.ulab.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

/**
 * Functional testing {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@DisplayName("Testing book functionality.")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

  private final Long userId = 1L;
  private final User user = User.builder()
      .id(userId)
      .fullName("Antoni Gaudi")
      .age(18)
      .title("Architect")
      .build();

  private final Long bookId = 1L;
  private final Book book = Book.builder()
      .id(1L)
      .title("Core Java Volume I Fundamentals")
      .author("Cay S. Horstmann")
      .user(user)
      .pageCount(928L)
      .build();

  @Captor
  ArgumentCaptor<Book> bookArgumentCaptor;

  @InjectMocks
  BookServiceImpl bookService;

  @Mock
  BookRepository bookRepository;

  private String getBookNotFoundMessage(Long bookId){

    return String.format("Book with id %d doesn't exist", bookId);
  }

  @Test
  void createBook_correctBookFormIsGiven_bookCreated() {

    //WHEN
    bookService.createBook(book);

    //THEN
    Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());

    assertEquals(book, bookArgumentCaptor.getValue());
  }

  @Test
  void updateBook_bookExists_bookIsUpdated() {

    //GIVEN
    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
    Book updatedBook = book;
    String newTitle = "Core Java Volume II – Advanced Features";
    updatedBook.setTitle(newTitle);

    //WHEN
    bookService.updateBook(updatedBook);

    //THEN
    Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());
    assertEquals(updatedBook, bookArgumentCaptor.getValue());
  }

  @Test
  void updateBook_bookDoesNotExists_bookNofFoundExceptionIsThrown() {

    //GIVEN
    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

    //WHEN
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> bookService.updateBook(book));

    //THEN
    assertEquals(BookNotFoundException.class, thrown.getClass());
    assertEquals(getBookNotFoundMessage(bookId), thrown.getMessage());
  }

  @Test
  void updateBook_givenBookIdEqualNull_bookNofFoundExceptionIsThrown() {

    //GIVEN
    Book bookWithWrongId = book;
    bookWithWrongId.setId(null);

    //WHEN
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> bookService.updateBook(bookWithWrongId));

    //THEN
    assertEquals(BookNotFoundException.class, thrown.getClass());
    assertEquals(getBookNotFoundMessage(null), thrown.getMessage());
  }

  @Test
  void getBookById_bookIdIsGiven_bookIsReturned() {

    //GIVEN
    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

    //WHEN
    Book bookReturned = bookService.getBookById(bookId);

    //THEN
    assertEquals(book, bookReturned);
  }

  @Test
  void getBookById_bookDoesNotExists_bookNofFoundExceptionIsThrown() {

    //GIVEN
    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

    //WHEN
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> bookService.getBookById(bookId));

    //THEN
    assertEquals(BookNotFoundException.class, thrown.getClass());
    assertEquals(getBookNotFoundMessage(bookId), thrown.getMessage());
  }

  @Test
  void deleteBookById_bookExists_bookIsDeleted() {

    //GIVEN
    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

    //WHEN
    bookService.deleteBookById(bookId);

    //THEN
    Mockito.verify(bookRepository).deleteById(bookId);
  }

  @Test
  void deleteBookById_bookDoesNotExists_bookNofFoundExceptionIsThrown() {

    //GIVEN
    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

    //WHEN
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> bookService.deleteBookById(bookId));

    //THEN
    assertEquals(BookNotFoundException.class, thrown.getClass());
    assertEquals(getBookNotFoundMessage(bookId), thrown.getMessage());
  }


  @Test
  void deleteBooksByUserId_booksExist_booksIsDeleted() {

    //GIVEN
    Long userId = 1L;

    //WHEN
    bookService.deleteBooksByUserId(userId);

    //THEN
    Mockito.verify(bookRepository).deleteAllByUserId(userId);
  }

  @Test
  void getBookListByUserId_booksExist_bookListIsReturned() {

    //GIVEN
    Long userId = 1L;
    List<Book> bookList = Collections.singletonList(book);
    Mockito.when(bookRepository.getAllByUserId(userId)).thenReturn(bookList);

    //WHEN
    List<Book> actual = bookService.getBookListByUserId(userId);

    //THEN
    Mockito.verify(bookRepository).getAllByUserId(userId);
    assertEquals(bookList, actual);
  }
}
