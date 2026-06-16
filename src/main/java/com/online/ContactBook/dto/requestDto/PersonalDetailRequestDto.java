package com.online.ContactBook.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalDetailRequestDto {

    private String fatherName;

    private String motherName;

    private String address;

}
