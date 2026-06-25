package com.online.ContactBook.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Captcha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String passKey;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String image;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isVerified=false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isUsed=false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isValid=true;

    @Column
    private LocalDateTime expiresAt;


}
