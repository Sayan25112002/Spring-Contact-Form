package com.online.ContactBook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = true)
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String fatherName;

    private String motherName;

    private String address;

    @OneToOne
    @JoinColumn(name = "memberId")
    @JsonIgnore
    private Member member;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "personalDetail",
            orphanRemoval = true
    )
    @Valid
    @Builder.Default
    @JsonIgnore
    private List<ContactGroup> contactGroups=new ArrayList<>();

}
