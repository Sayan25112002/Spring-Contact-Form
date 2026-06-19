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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("Incoming request:{}", request.getRequestURI());
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null||!authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            final String token = authHeader.substring(7);
            AccessToken accessToken = accessTokenRepository.findByAccessToken(token).orElseThrow(()->new RuntimeException("token not found"));
            if(!authUtil.isTokenExpired(token)&& !accessToken.getRevoked()){
                String memberName = authUtil.getMemberNameFromToken(token);
                if (memberName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Member member = memberRepository.findByUsername(memberName).orElseThrow();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception ex){
            handlerExceptionResolver.resolveException(request,response,null,ex);
        }
    }
}
