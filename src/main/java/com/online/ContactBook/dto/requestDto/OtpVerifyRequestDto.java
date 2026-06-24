package com.online.ContactBook.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpVerifyRequestDto {

    @NotBlank(message = "preAuthToken is required")
    private String preAuthToken;

    @NotBlank(message = "OTP is required")
    private String otp;

}
