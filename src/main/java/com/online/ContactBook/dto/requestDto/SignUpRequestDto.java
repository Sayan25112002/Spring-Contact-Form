package com.online.ContactBook.dto.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SignUpRequestDto {

    @NotBlank(message = "First Name is Required")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last Name is Required")
    private String lastName;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid Email Format")
    private String username;

    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid Phone Number"
    )
    private String phone;

    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^A-Za-z0-9]).{8,}$",
            message = "Password must have at least 1 uppercase letter, at least 1 lowercase letter, at least 1 digit, at least 1 special character and be at least 8 characters long"
    )
    private String password;

}
