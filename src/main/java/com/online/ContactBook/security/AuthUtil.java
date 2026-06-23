package com.online.ContactBook.security;

import com.online.ContactBook.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;

@Component
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Member member) {
        return Jwts.builder()
                .subject(member.getUsername())
                .claim("tokenType","ACCESS")
                .claim("memberId",member.getId().toString())
                .claim("role",member.getRole().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L*60*20))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(Member member) {
        return Jwts.builder()
                .claim("tokenType","REFRESH")
                .subject(member.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000L*60*60*24*7))
                .signWith(getSecretKey())
                .compact();
    }

    public String getMemberNameFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public Date getExpirationDateFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration();
    }

    public boolean isTokenExpired(String token){
        try{
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration().before(new Date());
        }catch (Exception e){
            return true;
        }
    }

    public String getTokenType(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("tokenType", String.class);
    }
}
