package com.online.ContactBook.dto.requestDto;

import com.online.ContactBook.entity.type.ContactType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDetailRequestDto {

    private ContactType contactType;

    private String mobile;

    private String phone;

    private String email;

    private String website;

    private String address;

}
