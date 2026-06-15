package com.online.ContactBook.service.implementation;

import com.online.ContactBook.dto.requestDto.LoginRequestDto;
import com.online.ContactBook.dto.requestDto.SignUpRequestDto;
import com.online.ContactBook.dto.respnseDto.LoginResponseDto;
import com.online.ContactBook.dto.respnseDto.SignUpResponseDto;
import com.online.ContactBook.repository.UserRepository;
import com.online.ContactBook.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @Override
    public SignUpResponseDto signUp(SignUpRequestDto memberRequestDto) {
        return null;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        return null;
    }
}
