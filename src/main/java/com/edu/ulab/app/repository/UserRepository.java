package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
