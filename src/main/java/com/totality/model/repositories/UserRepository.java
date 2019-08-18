package com.totality.model.repositories;

import com.totality.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmailId(String email);

  User findByUserName(String username);

  User findByUserId(long id);
}
