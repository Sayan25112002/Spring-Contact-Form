package com.online.ContactBook.repository;

import com.online.ContactBook.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken,Long> {
    Optional<AccessToken> findByAccessToken(String accessToken);

    List<AccessToken> findByMemberIdAndRevokedFalse(Long memberId);

    Boolean existsByMemberIdAndDeviceId(Long memberId, String deviceId);

    Optional<AccessToken> findByMemberIdAndDeviceIdAndRevokedFalse(Long memberId, String deviceId);

}
