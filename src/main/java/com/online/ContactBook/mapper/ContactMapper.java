package com.online.ContactBook.mapper;

import com.online.ContactBook.dto.requestDto.ContactRequestDto;
import com.online.ContactBook.dto.responseDto.ContactResponseDto;
import com.online.ContactBook.entity.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {ContactDetailMapper.class}
)
public interface ContactMapper {

    Contact toContact(ContactRequestDto contactRequestDto);

    @Mapping(source = "contactDetails", target = "contactDetailResponseDtos")
    ContactResponseDto toContactResponseDto(Contact contact);

    @Mapping(source = "contactDetails", target = "contactDetailResponseDtos")
    List<ContactResponseDto> toContactResponseDtos(List<Contact> contacts);

}
