package com.online.ContactBook.service;

import com.online.ContactBook.dto.requestDto.LoginRequestDto;
import com.online.ContactBook.dto.requestDto.SignUpRequestDto;
import com.online.ContactBook.dto.respnseDto.LoginResponseDto;
import com.online.ContactBook.dto.respnseDto.SignUpResponseDto;

public interface AuthenticationService {

    SignUpResponseDto signUp(SignUpRequestDto memberRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

}
