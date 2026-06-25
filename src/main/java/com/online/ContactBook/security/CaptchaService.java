package com.online.ContactBook.security;

import com.online.ContactBook.dto.responseDto.CaptchaResponseDto;
import com.online.ContactBook.entity.Captcha;
import com.online.ContactBook.exception.InvalidCaptchaException;
import com.online.ContactBook.repository.CaptchaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaRepository captchaRepository;

    private String generateCaptchaText(){
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder captchaText = new StringBuilder();
        for(int i = 0; i < 6; i++){
            captchaText.append(chars.charAt(random.nextInt(chars.length())));
        }
        return captchaText.toString();
    }

    private BufferedImage generateCaptchaImage(String text){
        int width = 200;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        Random random = new Random();
        for (int i = 0; i <= 10; i++) {
            g2d.setColor(new Color(random.nextInt(200),random.nextInt(200),random.nextInt(200)));
            int x1=random.nextInt(width);
            int y1=random.nextInt(height);
            int x2=random.nextInt(width);
            int y2=random.nextInt(height);
            g2d.drawLine(x1, y1, x2, y2);
        }
        for(int i=0;i<100;i++){
            g2d.setColor(new Color(random.nextInt(200),random.nextInt(200),random.nextInt(200)));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g2d.fillOval(x, y, 2, 2);
        }
        g2d.setFont(new Font("Times New Roman",Font.BOLD,48));
        g2d.setColor(Color.BLACK);
        g2d.drawString(text,40,60);
        g2d.dispose();
        return image;
    }

    private String convertToBase64(BufferedImage image,String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return String.format("data:image/%s;base64,%s",format,Base64.getEncoder().encodeToString(baos.toByteArray()));
    }

    public CaptchaResponseDto generateImageCaptcha(){
        List<Captcha> activeCaptchas = captchaRepository.findAll();
        activeCaptchas.forEach(captcha -> {
            captcha.setIsValid(false);
            captcha.setExpiresAt(LocalDateTime.now().plusSeconds(30));
        });
        captchaRepository.saveAll(activeCaptchas);
        String captchaText = generateCaptchaText();
        BufferedImage image = generateCaptchaImage(captchaText);
        String imageBase64=null;
        String format = "png";
        try{
            imageBase64=convertToBase64(image,format);
        }catch (Exception e){
            throw new RuntimeException("Failed to generate captcha image");
        }
        Captcha captcha = Captcha.builder()
                .passKey(UUID.randomUUID().toString())
                .answer(captchaText)
                .image(imageBase64)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();
        captchaRepository.save(captcha);
        return new CaptchaResponseDto(captcha.getPassKey(),captcha.getImage());
    }

    public void validateCaptcha(String passKey, String userAnswer){
        if(passKey==null||passKey.isBlank()||userAnswer==null||userAnswer.isBlank()){
            throw new InvalidCaptchaException("Captcha is Required");
        }
        Captcha captcha = captchaRepository.findByPassKey(passKey).orElseThrow(()->new InvalidCaptchaException("Captcha Expired Or Invalid"));
        if(captcha.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new InvalidCaptchaException("Captcha Expired,Please Request a New One");
        }
        captcha.setIsUsed(true);
        captchaRepository.save(captcha);
        if(!captcha.getAnswer().trim().equalsIgnoreCase(userAnswer.trim())){
            throw new InvalidCaptchaException("Incorrect Captcha Answer");
        }
        captcha.setIsVerified(true);
        captcha.setIsValid(false);
        captcha.setExpiresAt(LocalDateTime.now().plusSeconds(30));
        captchaRepository.save(captcha);

    }


}
