package com.edu.ulab.app.facade;

import com.edu.ulab.app.converter.BookConverter;
import com.edu.ulab.app.converter.UserConverter;
import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;
    private final BookConverter bookConverter;
    private final UserConverter userConverter;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper,
                          BookConverter bookConverter,
                          UserConverter userConverter) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
        this.bookConverter = bookConverter;
        this.userConverter = userConverter;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        User user = userService.createUser(userConverter.mapToEntity(userDto));
        UserDto createdUser = userConverter.mapToDto(user);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .map(bookConverter::mapToEntity)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(Book::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    /**
     * Retrieves an existing user by ID from the storage.
     * Updates the user's data with the received data.
     * Old books that the user had are deleted.
     * New books are added to the current user.
     */
    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest, Long userId) {

        log.info("Got user book update request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        userDto.setId(userId);
        log.info("Mapped user request: {}", userDto);

        User user = userService.updateUser(userConverter.mapToEntity(userDto));

        UserDto updatedUser = userConverter.mapToDto(user);
        log.info("Updated user: {}", updatedUser);

        bookService.deleteBooksByUserId(updatedUser.getId());

        List<Long> bookIdList = userBookRequest.getBookRequests()
            .stream()
            .filter(Objects::nonNull)
            .map(bookMapper::bookRequestToBookDto)
            .peek(bookDto -> bookDto.setUserId(updatedUser.getId()))
            .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
            .map(bookConverter::mapToEntity)
            .map(bookService::createBook)
            .peek(updateBook -> log.info("Updated book: {}", updateBook))
            .map(Book::getId)
            .toList();
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
            .userId(updatedUser.getId())
            .booksIdList(bookIdList)
            .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {

        log.info("Got user id: {}", userId);

        User user = userService.getUserById(userId);

        List<Long> bookIdList = bookService.getBookListByUserId(user.getId()).stream()
            .map(bookConverter::mapToDto)
            .map(BookDto::getId)
            .toList();
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
            .userId(user.getId())
            .booksIdList(bookIdList)
            .build();
    }

    public void deleteUserWithBooks(Long userId) {

        log.info("Got user id: {}", userId);
        userService.deleteUserById(userId);
        bookService.deleteBooksByUserId(userId);
    }
}
