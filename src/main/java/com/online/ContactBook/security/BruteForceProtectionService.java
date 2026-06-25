package com.online.ContactBook.security;

import com.online.ContactBook.entity.Member;
import com.online.ContactBook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BruteForceProtectionService {

    private final MemberRepository memberRepository;

    public void evaluateBeforeLogin(Member member) {
        if (!Boolean.TRUE.equals(member.getAccountLocked())) {
            return;
        }
        LocalDateTime unlockAt = unlockTime(member);
        if (LocalDateTime.now().isBefore(unlockAt)) {
            long remainingSeconds = Duration.between(LocalDateTime.now(), unlockAt).getSeconds();
            throw new LockedException("Account Temporarily Locked Due to multiple failed Attempts. Try Again after " + remainingSeconds + " second(s).");
        }
        member.setAccountLocked(false);
        memberRepository.save(member);
    }

    private LocalDateTime unlockTime(Member member) {
        long delaySeconds = (long)(30*Math.pow(2,member.getLockCount()));
        return member.getLockTime().plusSeconds(delaySeconds);
    }

    public void onLoginFailed(Member member) {
        int attempts = member.getFailedAttempts()+1;
        member.setFailedAttempts(attempts);
        if(attempts >= 5) {
            member.setAccountLocked(true);
            member.setLockTime(LocalDateTime.now());
            member.setLockCount(member.getLockCount()+1);
            member.setFailedAttempts(0);
        }
        memberRepository.save(member);
    }

    public void onLoginSuccess(Member member) {
        if(member.getFailedAttempts()!=0||Boolean.TRUE.equals(member.getAccountLocked())) {
            member.setFailedAttempts(0);
            member.setAccountLocked(false);
            member.setLockCount(0);
            member.setLockTime(null);
            memberRepository.save(member);
        }
    }
}
