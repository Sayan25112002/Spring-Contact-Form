package com.online.ContactBook.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalDetailResponseDto {

    private Long id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phone;

    private String fatherName;

    private String motherName;

    private String address;

    private List<ContactGroupResponseDto> contactGroupResponseDtoList;

}
