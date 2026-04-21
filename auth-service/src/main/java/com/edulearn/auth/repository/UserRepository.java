package com.edulearn.auth.repository;

import com.edulearn.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllByRole(String role);

    void deleteByUserId(Long userId);

    List<User> findByFullNameContaining(String name);
}
