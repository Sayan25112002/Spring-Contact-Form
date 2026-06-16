package com.online.ContactBook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;

    private String image;

    @ManyToMany
    @JoinTable(
            name = "contactContactGroup",
            joinColumns = @JoinColumn(name = "contactId"),
            inverseJoinColumns = @JoinColumn(name = "contactGroupId")
    )
    private List<ContactGroup> contactGroups;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contact")
    private List<ContactDetail> contactDetails;

}
