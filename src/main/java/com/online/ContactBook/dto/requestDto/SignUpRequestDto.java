package com.online.ContactBook.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDto {

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phone;

    private String password;

}
