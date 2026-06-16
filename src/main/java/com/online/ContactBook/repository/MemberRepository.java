package com.online.ContactBook.repository;

import com.online.ContactBook.entity.Member;
import com.online.ContactBook.entity.type.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByRole(Role role);

    Optional<Member> findByUsername(String username);
}
