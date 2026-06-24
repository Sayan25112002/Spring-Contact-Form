package com.online.ContactBook.security;

import com.online.ContactBook.dto.responseDto.CaptchaResponseDto;
import com.online.ContactBook.entity.Captcha;
import com.online.ContactBook.exception.InvalidCaptchaException;
import com.online.ContactBook.repository.CaptchaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaRepository captchaRepository;

    public CaptchaResponseDto generateCaptcha(){
        Random random = new Random();
        int a = random.nextInt(10)+1;
        int b = random.nextInt(10)+1;
        String question = a + "+" + b + "=?";
        String answer = String.valueOf(a+b);
        Captcha captcha = Captcha.builder()
                .id(UUID.randomUUID().toString())
                .answer(answer)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();
        captchaRepository.save(captcha);
        return new CaptchaResponseDto(captcha.getId(),question);
    }

    public void validateCaptcha(String captchaId, String userAnswer){
        if(captchaId==null||captchaId.isBlank()||userAnswer==null||userAnswer.isBlank()){
            throw new InvalidCaptchaException("Captcha is Required");
        }
        Captcha captcha = captchaRepository.findById(captchaId).orElseThrow(()->new InvalidCaptchaException("Captcha Expired Or Invalid"));
        captchaRepository.delete(captcha);
        if(captcha.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new InvalidCaptchaException("Captcha Expired,Please Request a New One");
        }
        if(!captcha.getAnswer().trim().equalsIgnoreCase(userAnswer.trim())){
            throw new InvalidCaptchaException("Incorrect Captcha Answer");
        }
    }
}
