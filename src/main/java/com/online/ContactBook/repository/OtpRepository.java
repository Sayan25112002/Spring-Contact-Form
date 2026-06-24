package com.online.ContactBook.repository;

import com.online.ContactBook.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OtpToken, String> {
}
