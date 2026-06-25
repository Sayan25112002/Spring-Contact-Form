package com.online.ContactBook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
    private String id;

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
