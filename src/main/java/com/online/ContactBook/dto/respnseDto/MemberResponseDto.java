package com.online.ContactBook.dto.respnseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {

    private Long id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phone;

    private String password;

    private String role;

}
