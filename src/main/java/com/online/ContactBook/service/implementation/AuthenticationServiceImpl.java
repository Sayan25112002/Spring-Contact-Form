package com.online.ContactBook.service.implementation;

import com.online.ContactBook.dto.requestDto.LoginRequestDto;
import com.online.ContactBook.dto.requestDto.SignUpRequestDto;
import com.online.ContactBook.dto.respnseDto.LoginResponseDto;
import com.online.ContactBook.dto.respnseDto.SignUpResponseDto;
import com.online.ContactBook.entity.Member;
import com.online.ContactBook.entity.type.Role;
import com.online.ContactBook.repository.MemberRepository;
import com.online.ContactBook.security.AuthUtil;
import com.online.ContactBook.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;
    private final MemberRepository memberRepository;

    @Override
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        Member member = memberRepository.findByUsername(signUpRequestDto.getUsername()).orElse(null);
        if(member!=null){
            throw new IllegalArgumentException("User already exists. Please Login");
        }
        member=memberRepository.save(Member.builder()
                .firstName(signUpRequestDto.getFirstName())
                .middleName(signUpRequestDto.getMiddleName())
                .lastName(signUpRequestDto.getLastName())
                .username(signUpRequestDto.getUsername())
                .phone(signUpRequestDto.getPhone())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .role(Role.REGULAR_MEMBER)
                .build()
        );
        return new SignUpResponseDto(
                member.getId(),
                member.getFirstName(),
                member.getMiddleName(),
                member.getLastName(),
                member.getUsername(),
                member.getPhone(),
                member.getPassword(),
                member.getRole().toString()
        );
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );
        Member member = (Member) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(member);
        return new LoginResponseDto(member.getId(),token);
    }
}
