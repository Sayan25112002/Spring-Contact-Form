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
public class IpLoginAttempt {

    @Id
    private String ipAddress;

    @Column(nullable = false)
    @Builder.Default
    private Integer failedAttempts=0;

    private LocalDateTime lockedUntil;

}
