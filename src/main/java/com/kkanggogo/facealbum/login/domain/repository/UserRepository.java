package com.kkanggogo.facealbum.login.domain.repository;

import com.kkanggogo.facealbum.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM User WHERE username= ?1", nativeQuery = true)
    User searchUsername(String username);

    @Query(value = "SELECT * FROM User WHERE userId= ?1", nativeQuery = true)
    Optional<User> searchId(Long id);

    @Query(value = "SELECT COUNT(id) FROM User")
    Integer getCount();
}
