package com.online.ContactBook.security;

import com.online.ContactBook.entity.AccessToken;
import com.online.ContactBook.repository.AccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RevokeExpiredToken {

    private final AccessTokenRepository accessTokenRepository;

    @Scheduled(fixedRate = 1000)
    public void revokeTokenIfExpired() {
        List<AccessToken> accessTokens = accessTokenRepository.findAll();
        for (AccessToken accessToken : accessTokens) {
            if(accessToken.getExpiresIn().before(new Date())) {
                accessToken.setRevoked(true);
            }
        }
        accessTokenRepository.saveAll(accessTokens);
    }

}
