package com.online.ContactBook.config;

import com.online.ContactBook.entity.Member;
import com.online.ContactBook.entity.type.Role;
import com.online.ContactBook.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if(!memberRepository.existsByRole(Role.ADMINISTRATOR)){
            Member admin = Member.builder()
                    .firstName("System")
                    .middleName("")
                    .lastName("Administrator")
                    .username("ocb@domain.com")
                    .phone("1234567890")
                    .password(passwordEncoder.encode("Ocb@123456"))
                    .role(Role.ADMINISTRATOR)
                    .failedAttempts(0)
                    .accountLocked(Boolean.FALSE)
                    .lockTime(null)
                    .build();
            memberRepository.save(admin);
        }
    }
}
