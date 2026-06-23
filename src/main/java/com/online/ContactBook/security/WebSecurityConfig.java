package com.online.ContactBook.security;

import com.online.ContactBook.entity.AccessToken;
import com.online.ContactBook.repository.AccessTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AccessTokenRepository accessTokenRepository;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandlerConfig ->
                                exceptionHandlerConfig
                                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement(sessionManagementConfig ->
                        sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout ->logout
                        .logoutUrl("/auth/logout")
                        .addLogoutHandler((request, response, authentication) ->{
                            String authHeader = request.getHeader("Authorization");
                            if(authHeader != null && authHeader.startsWith("Bearer ")){
                                String token = authHeader.substring(7);
                                AccessToken accessToken = accessTokenRepository
                                        .findByAccessToken(token)
                                        .orElse(null);
                                if(accessToken==null){
                                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                    response.setContentType("application/json");
                                    try {
                                        response.getWriter().write("""
                                                {
                                                    "status":404,
                                                    "message":"Access Token Not Found"
                                                }
                                                """);
                                        response.flushBuffer();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return;
                                }
                                else{
                                    if(accessToken.getRevoked()){
                                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                        response.setContentType("application/json");
                                        try {
                                            response.getWriter().write("""
                                                     {
                                                         "status":400,
                                                         "message":"Access Token Already Revoked/Expired"
                                                     }
                                                     """);
                                            response.flushBuffer();
                                        }catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        return;
                                    }
                                    accessToken.setRevoked(true);
                                    accessTokenRepository.save(accessToken);
                                }
                            }
                            else {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                response.setContentType("application/json");
                                try {
                                    response.getWriter().write("""
                                            {
                                                "status":"400",
                                                "message":"Invalid Access Token"
                                            }
                                            """);
                                    response.flushBuffer();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        })
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if(response.isCommitted()){
                                return;
                            }
                            SecurityContextHolder.clearContext();
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("""
                                    {
                                        "status": 200,
                                        "message": "Logout Successful"
                                    }
                                    """);
                        })
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
