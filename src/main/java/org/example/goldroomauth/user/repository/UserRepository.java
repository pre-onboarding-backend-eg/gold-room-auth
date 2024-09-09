package org.example.goldroomauth.user.repository;

import org.example.goldroomauth.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // username으로 사용자 찾기
    Optional<User> findByUsername(String username);
}
