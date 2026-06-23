package com.online.ContactBook.service.implementation;

import com.online.ContactBook.dto.requestDto.LoginRequestDto;
import com.online.ContactBook.dto.requestDto.SignUpRequestDto;
import com.online.ContactBook.dto.responseDto.LoginResponseDto;
import com.online.ContactBook.dto.responseDto.RefreshTokenResponseDto;
import com.online.ContactBook.dto.responseDto.SignUpResponseDto;
import com.online.ContactBook.entity.AccessToken;
import com.online.ContactBook.entity.Member;
import com.online.ContactBook.entity.type.Role;
import com.online.ContactBook.repository.AccessTokenRepository;
import com.online.ContactBook.repository.MemberRepository;
import com.online.ContactBook.security.AuthUtil;
import com.online.ContactBook.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;
    private final MemberRepository memberRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final AccessDeniedHandler accessDeniedHandler;

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
    public LoginResponseDto login(LoginRequestDto loginRequestDto, String deviceId) {
        if(!memberRepository.existsByUsername(loginRequestDto.getUsername())){
            throw new BadCredentialsException("Email or Password is invalid/User Not Found");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );
        Member member = (Member) authentication.getPrincipal();
        String finalDeviceId;
        if(deviceId!=null && !deviceId.isBlank() && accessTokenRepository.existsByMemberIdAndDeviceId(member.getId(),deviceId)){
            finalDeviceId=deviceId;
        }
        else {
            finalDeviceId=UUID.randomUUID().toString();
        }
        String accessToken = authUtil.generateAccessToken(member);
        String refreshToken = authUtil.generateRefreshToken(member);
        AccessToken accessToken1 = new AccessToken();
        accessToken1.setAccessToken(accessToken);
        accessToken1.setExpiresIn(authUtil.getExpirationDateFromToken(accessToken));
        accessToken1.setRevoked(false);
        accessToken1.setDeviceId(finalDeviceId);
        accessToken1.setMemberId(member.getId());
        accessTokenRepository.save(accessToken1);
        return new LoginResponseDto(member.getId(),accessToken,refreshToken,finalDeviceId);
    }

    @Override
    public RefreshTokenResponseDto refreshAccessToken(String refreshToken, String deviceId) {
        String tokenType = authUtil.getTokenType(refreshToken);
        if(!"REFRESH".equals(tokenType)){
            throw new BadCredentialsException("Only Refresh Token is supported");
        }
        if(refreshToken==null || authUtil.isTokenExpired(refreshToken)){
            throw new BadCredentialsException("Invalid Refresh Token");
        }
        if(deviceId==null || deviceId.isBlank()){
            throw new BadCredentialsException("Device ID is required");
        }
        String userName = authUtil.getMemberNameFromToken(refreshToken);
        Member member = memberRepository.findByUsername(userName).orElseThrow(()->new UsernameNotFoundException("User not found"));
        Boolean isDeviceRecognized = accessTokenRepository.existsByMemberIdAndDeviceId(member.getId(), deviceId);
        if(!isDeviceRecognized){
            throw new BadCredentialsException("Device ID is not recognized for this member");
        }
        List<AccessToken> activeTokens = accessTokenRepository.findByMemberIdAndRevokedFalse(member.getId());
        for(AccessToken oldAccessToken : activeTokens){
            oldAccessToken.setRevoked(true);
        }
        accessTokenRepository.saveAll(activeTokens);
        String newAccessToken = authUtil.generateAccessToken(member);
        AccessToken accessToken1 = new AccessToken();
        accessToken1.setAccessToken(newAccessToken);
        accessToken1.setExpiresIn(authUtil.getExpirationDateFromToken(newAccessToken));
        accessToken1.setRevoked(false);
        accessToken1.setDeviceId(deviceId);
        accessToken1.setMemberId(member.getId());
        accessTokenRepository.save(accessToken1);
        return new RefreshTokenResponseDto(newAccessToken);
    }


}
