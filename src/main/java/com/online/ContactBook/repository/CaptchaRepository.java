package com.online.ContactBook.repository;

import com.online.ContactBook.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, String> {
}
