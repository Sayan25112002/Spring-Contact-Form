package com.online.ContactBook.security;

import com.online.ContactBook.entity.IpLoginAttempt;
import com.online.ContactBook.exception.IpBlockedException;
import com.online.ContactBook.repository.IpLoginRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IpRateLimitService {

    private final IpLoginRepository ipLoginRepository;

    public String resolveIp(HttpServletRequest request){
        String forwarded = request.getHeader("X-Forwarded-For");
        if(forwarded!=null && !forwarded.isBlank()){
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    public void evaluateBeforeLogin(String ip){
        IpLoginAttempt loginAttempt = ipLoginRepository.findById(ip).orElse(null);
        if(loginAttempt==null||loginAttempt.getLockedUntil()==null){
            return;
        }
        if(LocalDateTime.now().isBefore(loginAttempt.getLockedUntil())){
            long remainingSeconds = Duration.between(LocalDateTime.now(), loginAttempt.getLockedUntil()).getSeconds();
            throw new IpBlockedException("Too many login attempts from this network. Try again in "+remainingSeconds+" second(s).");

        }
    }

    public void onLoginFailed(String ip){
        IpLoginAttempt loginAttempt = ipLoginRepository.findById(ip)
                .orElse(IpLoginAttempt.builder()
                        .ipAddress(ip)
                        .failedAttempts(0)
                        .build());
        int attempts = loginAttempt.getFailedAttempts()+1;
        loginAttempt.setFailedAttempts(attempts);
        if(attempts >= 10){
            int overflow = attempts - 10;
            long delaySeconds = (long)(30*Math.pow(2, overflow));
            loginAttempt.setLockedUntil(LocalDateTime.now().plusSeconds(delaySeconds));
        }
        ipLoginRepository.save(loginAttempt);
    }

    public void onLoginSuccess(String ip){
        ipLoginRepository.findById(ip).ifPresent(loginAttempt -> {
            loginAttempt.setFailedAttempts(0);
            loginAttempt.setLockedUntil(null);
            ipLoginRepository.save(loginAttempt);
        });
    }
}
