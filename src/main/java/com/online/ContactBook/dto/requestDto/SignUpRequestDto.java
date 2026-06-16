package com.online.ContactBook.dto.requestDto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SignUpRequestDto {

    private String firstName;

    private String middleName;

    private String lastName;

    private String username;

    private String phone;

    private String password;

}
