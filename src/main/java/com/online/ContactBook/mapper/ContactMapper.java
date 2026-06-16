package com.online.ContactBook.mapper;

import com.online.ContactBook.dto.requestDto.ContactRequestDto;
import com.online.ContactBook.dto.responseDto.ContactResponseDto;
import com.online.ContactBook.entity.Contact;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    Contact toContact(ContactRequestDto contactRequestDto);

    ContactResponseDto toContactResponseDto(Contact contact);

    List<ContactResponseDto> toContactResponseDtos(List<Contact> contacts);

}
