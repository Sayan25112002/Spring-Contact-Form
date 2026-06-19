package com.online.ContactBook.repository;

import com.online.ContactBook.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken,Long> {
    Optional<AccessToken> findByAccessToken(String accessToken);
}
