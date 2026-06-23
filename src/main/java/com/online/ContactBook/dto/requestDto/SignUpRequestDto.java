package com.online.ContactBook.dto.requestDto;

import jakarta.validation.constraints.Pattern;
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

    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^A-Za-z0-9]).{8,}$",
            message = "Password must have at least 1 uppercase letter, at least 1 lowercase letter, at least 1 digit, at least 1 special character and be at least 8 characters long"
    )
    private String password;

}
