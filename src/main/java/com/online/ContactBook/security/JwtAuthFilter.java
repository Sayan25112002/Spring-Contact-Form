package com.online.ContactBook.security;

import com.online.ContactBook.entity.AccessToken;
import com.online.ContactBook.entity.Member;
import com.online.ContactBook.repository.AccessTokenRepository;
import com.online.ContactBook.repository.MemberRepository;
import jakarta.persistence.Access;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final AuthUtil authUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AccessTokenRepository accessTokenRepository;

    private void writeErrorMessage(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("""
                {
                    "status": 401,
                    "error": "Unauthorized",
                    "message":"%s"
                }
                """.formatted(message)
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if(uri.equals("/auth/login") || uri.equals("/auth/signup") || uri.equals("/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Incoming request:{}", request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null||!authHeader.startsWith("Bearer ")) {
            writeErrorMessage(response, "Invalid JWT Token");
            return;
        }
        final String token = authHeader.substring(7);
        AccessToken accessToken = accessTokenRepository.findByAccessToken(token).orElse(null);
        if (accessToken == null) {
            writeErrorMessage(response, "JWT Token not found");
            return;
        }
        if(authUtil.isTokenExpired(token)){
            accessToken.setRevoked(true);
            accessTokenRepository.save(accessToken);
            writeErrorMessage(response, "JWT Token expired");
            return;
        }
        if(accessToken.getRevoked()){
            writeErrorMessage(response, "JWT Token already revoked");
            return;
        }
        String memberName = authUtil.getMemberNameFromToken(token);
        if (memberName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Member member = memberRepository.findByUsername(memberName).orElseThrow();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}

