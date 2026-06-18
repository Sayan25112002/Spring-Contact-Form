package com.online.ContactBook.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionManagementConfig ->
                        sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/createPersonalDetail/**",
                                "/createContactGroup/**",
                                "/createContact/**",
                                "/createContactDetails/**").hasRole("REGULAR_MEMBER")
                        .requestMatchers(HttpMethod.PATCH,
                                "/updatePersonalDetail/**",
                                "/updateContactGroup/**",
                                "/updateContact/**",
                                "/updateContactDetail/**").hasRole("REGULAR_MEMBER")
                        .requestMatchers(HttpMethod.GET,
                                "/getPersonalDetail/**").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE,
                                "/deletePersonalDetail/**",
                                "/deleteContactGroup/**",
                                "/deleteContact/**",
                                "/deleteContactDetail/**").hasRole("ADMINISTRATOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
