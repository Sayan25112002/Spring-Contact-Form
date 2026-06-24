package com.online.ContactBook.controller;

import com.online.ContactBook.dto.requestDto.LoginRequestDto;
import com.online.ContactBook.dto.requestDto.OtpVerifyRequestDto;
import com.online.ContactBook.dto.requestDto.SignUpRequestDto;
import com.online.ContactBook.dto.responseDto.*;
import com.online.ContactBook.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.ok().body(authenticationService.signUp(signUpRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginInitResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto,HttpServletRequest request) {
        LoginInitResponseDto loginInitResponseDto = authenticationService.login(loginRequestDto,request);
        return ResponseEntity.ok().body(loginInitResponseDto);
    }

    @PostMapping("/login/verify-otp")
    public ResponseEntity<LoginResponseDto> verifyOtpAndLogin(@Valid @RequestBody OtpVerifyRequestDto otpVerifyRequestDto,
                                                                  @RequestHeader(value = "Device-Id",required = false) String deviceId) {
        LoginResponseDto loginResponseDto = authenticationService.verifyOtpAndLogin(otpVerifyRequestDto,deviceId);
        return ResponseEntity.ok()
                .header("Authorization","Bearer "+loginResponseDto.getAccessToken())
                .header("Refresh-Token", loginResponseDto.getRefreshToken())
                .header("Device-Id",loginResponseDto.getDeviceId())
                .body(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refresh(@RequestHeader("Refresh-Token")  String refreshToken,
                                                           @RequestHeader("Device-Id") String deviceId) {
        RefreshTokenResponseDto tokenResponseDto = authenticationService.refreshAccessToken(refreshToken,deviceId);
        return ResponseEntity.ok()
                .header("Authorization","Bearer "+tokenResponseDto.getAccessToken())
                .body(tokenResponseDto);
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponseDto> getCaptcha() {
        return ResponseEntity.ok(authenticationService.generateCaptcha());
    }

}
