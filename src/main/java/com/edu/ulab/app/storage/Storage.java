package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import java.util.HashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Storage {
  //todo создать хранилище в котором будут содержаться данные
  // сделать абстракции через которые можно будет производить операции с хранилищем
  // продумать логику поиска и сохранения
  // продумать возможные ошибки
  // учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
  // продумать что у узера может быть много книг и нужно создать эту связь
  // так же учесть, что методы хранилища принимают другой тип данных - учесть это в абстракции

  private final HashMap<Long, User> storageOfUsers = new HashMap<>();
  private final HashMap<Long, Book> storageOfBooks = new HashMap<>();
}
