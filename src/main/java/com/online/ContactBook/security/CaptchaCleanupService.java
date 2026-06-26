package com.online.ContactBook.security;

import com.online.ContactBook.repository.CaptchaRepository;
import com.online.ContactBook.repository.OtpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CaptchaCleanupService {

    private final CaptchaRepository captchaRepository;
    private final OtpRepository otpRepository;

    @Transactional
    @Scheduled(fixedRate=30000)
    public void cleanupExpiredCaptcha(){

        captchaRepository.deleteByIsVerifiedTrueAndIsUsedTrueAndIsValidFalseAndExpiresAtBefore(LocalDateTime.now());

        captchaRepository.deleteByIsValidFalseAndExpiresAtBefore(LocalDateTime.now());

        otpRepository.deleteByIsValidFalseAndExpiresAtBefore(LocalDateTime.now());

    }
}
