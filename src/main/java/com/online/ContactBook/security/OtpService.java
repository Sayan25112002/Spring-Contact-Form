package com.online.ContactBook.security;

import com.online.ContactBook.entity.OtpToken;
import com.online.ContactBook.exception.InvalidOtpException;
import com.online.ContactBook.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpRepository otpRepository;

    public String generateOtp(String username){
        List<OtpToken> otpTokenList = otpRepository.findAll();
        for(OtpToken otpToken : otpTokenList){
            otpToken.setIsValid(false);
            otpToken.setExpiresAt(LocalDateTime.now().plusSeconds(30));
            if(otpToken.getNow().isAfter(otpToken.getExpiresAt())){
                otpRepository.delete(otpToken);
            }
        }
        otpRepository.saveAll(otpTokenList);
        String otp = String.format("%06d",new Random().nextInt(1_00_000));
        String preAuthToken = UUID.randomUUID().toString();
        OtpToken otpToken = OtpToken.builder()
                .preAuthToken(preAuthToken)
                .username(username)
                .otp(otp)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .attempts(0)
                .build();
        otpRepository.save(otpToken);
        log.info("OTP for user '{}' is {} (valid for 5 minutes)",username,otp);
        return preAuthToken;
    }

    public String validateOtpAndGetUserName(String preAuthToken, String otp){
        if(preAuthToken==null || preAuthToken.isBlank()){
            throw new InvalidOtpException("Login Session is Invalid, Please Login Again");
        }
        OtpToken otpToken = otpRepository.findByPreAuthToken(preAuthToken)
                .orElseThrow(() -> new InvalidOtpException("Login Session Expired, Please Login Again"));
        if(otpToken.getExpiresAt().isBefore(LocalDateTime.now())||(otpToken.getIsValid().equals(false))){
            otpRepository.delete(otpToken);
            throw new InvalidOtpException("OTP expired, Please Login Again");
        }
        if(otpToken.getAttempts()>=5){
            otpToken.setIsValid(false);
            otpToken.setExpiresAt(LocalDateTime.now().plusSeconds(30));
            otpRepository.save(otpToken);
            throw new InvalidOtpException("Too many incorrect Otp Request, Please Login Again");
        }
        if(!otpToken.getOtp().equals(otp)){
            otpToken.setAttempts(otpToken.getAttempts() + 1);
            otpRepository.save(otpToken);
            throw new InvalidOtpException("Incorrect Otp");
        }
        otpToken.setIsValid(false);
        otpToken.setExpiresAt(LocalDateTime.now().plusSeconds(30));
        otpRepository.save(otpToken);
        return otpToken.getUsername();
    }
}
