package com.online.ContactBook.service;

import com.online.ContactBook.dto.requestDto.LoginRequestDto;
import com.online.ContactBook.dto.requestDto.OtpVerifyRequestDto;
import com.online.ContactBook.dto.requestDto.SignUpRequestDto;
import com.online.ContactBook.dto.responseDto.*;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    SignUpResponseDto signUp(SignUpRequestDto memberRequestDto);

    LoginInitResponseDto login(LoginRequestDto loginRequestDto, HttpServletRequest request);

    LoginResponseDto verifyOtpAndLogin(OtpVerifyRequestDto otpVerifyRequestDto, String deviceIdHeader);

    RefreshTokenResponseDto refreshAccessToken(String refreshToken, String deviceId);

    CaptchaResponseDto generateCaptcha();

}
