package com.online.ContactBook.repository;

import com.online.ContactBook.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByPreAuthToken(String preAuthToken);

    void deleteByIsValidFalseAndExpiresAtBefore(LocalDateTime expiresAt);

}

