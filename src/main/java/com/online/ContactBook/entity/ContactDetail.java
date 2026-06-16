package com.online.ContactBook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.online.ContactBook.entity.type.ContactType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ContactDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    private String mobile;

    private String phone;

    private String email;

    private String website;

    private String address;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    @JsonIgnore
    private Contact contact;

}
