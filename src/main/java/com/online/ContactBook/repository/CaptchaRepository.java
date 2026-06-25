package com.online.ContactBook.repository;

import com.online.ContactBook.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, String> {

    void deleteByIsValidFalseAndExpiresAtBefore(LocalDateTime expiredTime);

    void deleteByIsVerifiedTrueAndIsUsedTrueAndIsValidFalseAndExpiresAtBefore(LocalDateTime expiredTime);

    List<Captcha> findByIsVerifiedTrueAndIsUsedTrue();

}
