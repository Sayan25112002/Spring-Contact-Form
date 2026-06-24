package com.online.ContactBook.repository;

import com.online.ContactBook.entity.IpLoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpLoginRepository extends JpaRepository<IpLoginAttempt,String> {
}
