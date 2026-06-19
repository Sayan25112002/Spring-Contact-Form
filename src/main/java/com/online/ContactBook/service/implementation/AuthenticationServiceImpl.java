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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;
    private final MemberRepository memberRepository;
    private final AccessTokenRepository accessTokenRepository;

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
        String accessToken = authUtil.generateAccessToken(member);
        String refreshToken = authUtil.generateRefreshToken(member);
        AccessToken accessToken1 = new AccessToken();
        accessToken1.setAccessToken(accessToken);
        accessToken1.setExpiresIn(authUtil.getExpirationDateFromToken(accessToken));
        accessToken1.setRevoked(false);
        accessTokenRepository.save(accessToken1);
        return new LoginResponseDto(member.getId(),accessToken,refreshToken);
    }

    @Override
    public RefreshTokenResponseDto refreshAccessToken(String refreshToken) {
        if(refreshToken==null || authUtil.isTokenExpired(refreshToken)){
            throw new BadCredentialsException("Invalid Refresh Token");
        }
        String userName = authUtil.getMemberNameFromToken(refreshToken);
        Member member = memberRepository.findByUsername(userName).orElseThrow(()->new UsernameNotFoundException("User not found"));
        String newAccessToken = authUtil.generateAccessToken(member);
        AccessToken accessToken1 = new AccessToken();
        accessToken1.setAccessToken(newAccessToken);
        accessToken1.setExpiresIn(authUtil.getExpirationDateFromToken(newAccessToken));
        accessToken1.setRevoked(false);
        accessTokenRepository.save(accessToken1);
        return new RefreshTokenResponseDto(newAccessToken);
    }

    @Override
    public void logout() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new BadCredentialsException("Invalid Token/Token Not Found");
        }
        String token = authHeader.substring(7);
        AccessToken accessToken = accessTokenRepository
                .findByAccessToken(token)
                .orElseThrow(()->new BadCredentialsException("No Token Found"));
        accessToken.setRevoked(true);
        accessTokenRepository.save(accessToken);
        SecurityContextHolder.clearContext();

    }

}
