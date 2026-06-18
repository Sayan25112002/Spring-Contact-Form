package com.online.ContactBook.dto.responseDto;

import com.online.ContactBook.entity.Contact;
import com.online.ContactBook.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactGroupResponseDto {

    private Long id;

    private String groupName;

    private List<ContactResponseDto> contactResponseDtos;

}
