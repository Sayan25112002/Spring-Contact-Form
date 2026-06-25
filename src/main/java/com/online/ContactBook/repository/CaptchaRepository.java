package com.online.ContactBook.repository;

import com.online.ContactBook.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, Long> {

    void deleteByIsValidFalseAndExpiresAtBefore(LocalDateTime expiredTime);

    void deleteByIsVerifiedTrueAndIsUsedTrueAndIsValidFalseAndExpiresAtBefore(LocalDateTime expiredTime);

    Optional<Captcha> findByPassKey(String passKey);

}
