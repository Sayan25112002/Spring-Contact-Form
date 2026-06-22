package com.online.ContactBook.service;

import com.online.ContactBook.dto.requestDto.LoginRequestDto;
import com.online.ContactBook.dto.requestDto.SignUpRequestDto;
import com.online.ContactBook.dto.responseDto.LoginResponseDto;
import com.online.ContactBook.dto.responseDto.RefreshTokenResponseDto;
import com.online.ContactBook.dto.responseDto.SignUpResponseDto;

public interface AuthenticationService {

    SignUpResponseDto signUp(SignUpRequestDto memberRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    RefreshTokenResponseDto refreshAccessToken(String refreshToken);

}
