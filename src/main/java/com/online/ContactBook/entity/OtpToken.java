package com.online.ContactBook.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Builder.Default
    private String preAuthToken= UUID.randomUUID().toString();

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String otp;

    @Column(nullable=false)
    @Builder.Default
    private Boolean isValid=true;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime now = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder.Default
    @Column(nullable = false)
    private Integer attempts = 0;

}
