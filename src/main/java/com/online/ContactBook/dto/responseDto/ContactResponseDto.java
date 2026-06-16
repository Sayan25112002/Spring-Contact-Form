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
public class ContactResponseDto {

    private Long id;

    private String name;

    private String image;

    private List<ContactGroupResponseDto> contactGroupResponseDtos;

    private List<ContactDetailResponseDto> contactDetailResponseDtos;

}
