package com.online.ContactBook.dto.responseDto;

import com.online.ContactBook.entity.Contact;
import com.online.ContactBook.entity.type.ContactType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDetailResponseDto {

    private Long id;

    private ContactType contactType;

    private String mobile;

    private String phone;

    private String email;

    private String website;

    private String address;

    private Contact contact;

}
