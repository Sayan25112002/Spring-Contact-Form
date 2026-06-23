package com.online.ContactBook.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class AccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String accessToken;

    private Date expiresIn;

    private String deviceId;

    private Long memberId;

    private Boolean revoked;

}
