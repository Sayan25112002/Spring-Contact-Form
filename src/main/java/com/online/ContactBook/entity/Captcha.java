package com.online.ContactBook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    @Column(nullable = false)
    private LocalDateTime expiresAt;

}
