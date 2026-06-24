package com.online.ContactBook.controller;

import com.online.ContactBook.dto.requestDto.LoginRequestDto;
import com.online.ContactBook.dto.requestDto.SignUpRequestDto;
import com.online.ContactBook.dto.responseDto.LoginResponseDto;
import com.online.ContactBook.dto.responseDto.RefreshTokenResponseDto;
import com.online.ContactBook.dto.responseDto.SignUpResponseDto;
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
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                                  @RequestHeader(value = "Device-Id", required = false) String deviceId,
                                                    HttpServletRequest request) {
        LoginResponseDto loginResponseDto = authenticationService.login(loginRequestDto,deviceId,request);
        return ResponseEntity.ok()
                .header("Authorization","Bearer "+loginResponseDto.getAccessToken())
                .header("Refresh-Token",loginResponseDto.getRefreshToken())
                .header("Device-ID",loginResponseDto.getDeviceId())
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

}
