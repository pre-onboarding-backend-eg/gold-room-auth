package org.example.goldroomauth.user.repository;

import org.example.goldroomauth.user.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUser_UserId(@Param("userId") UUID userId);
}
