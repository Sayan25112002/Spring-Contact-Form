package com.online.ContactBook.dto.requestDto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDto {

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phone;

    private String password;

}
