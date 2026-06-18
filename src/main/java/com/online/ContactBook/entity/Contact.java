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
@Builder
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    @ManyToMany
    @JoinTable(
            name = "contactContactGroup",
            joinColumns = @JoinColumn(name = "contactId"),
            inverseJoinColumns = @JoinColumn(name = "contactGroupId")
    )
    @JsonIgnore
    private List<ContactGroup> contactGroups;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "contact",
            orphanRemoval = true
    )
    @JsonIgnore
    private List<ContactDetail> contactDetails;

}
