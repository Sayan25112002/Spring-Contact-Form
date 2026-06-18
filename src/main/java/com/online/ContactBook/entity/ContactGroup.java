package com.online.ContactBook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String groupName;

    @ManyToOne
    @JoinColumn(name = "personalDetailId")
    @JsonIgnore
    private PersonalDetail personalDetail;

    @ManyToMany(
            mappedBy = "contactGroups"
    )
    @JsonIgnore
    private List<Contact> contacts;

}
