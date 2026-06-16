package com.online.ContactBook.repository;

import com.online.ContactBook.entity.PersonalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalDetailRepository extends JpaRepository<PersonalDetail, Long> {
}
