package com.questnr.model.repositories;

import com.questnr.model.entities.User;
import com.questnr.model.projections.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmailId(String email);

  User findByUserName(String username);

  User findByUserId(long id);

  @Query("Select user from User user where user.fullName like %:userString%")
  List<UserProjection> findByFullNameContaining(String userString);
}
