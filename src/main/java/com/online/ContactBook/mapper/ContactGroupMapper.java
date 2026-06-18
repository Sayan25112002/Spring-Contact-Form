package com.online.ContactBook.mapper;

import com.online.ContactBook.dto.requestDto.ContactGroupRequestDto;
import com.online.ContactBook.dto.responseDto.ContactGroupResponseDto;
import com.online.ContactBook.entity.ContactGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {ContactMapper.class}
)
public interface ContactGroupMapper {

    ContactGroup toContactGroup(ContactGroupRequestDto contactGroupRequestDto);

    @Mapping(source = "contacts", target = "contactResponseDtos")
    ContactGroupResponseDto toContactGroupResponseDto(ContactGroup contactGroup);

    @Mapping(source = "contacts", target = "contactResponseDtos")
    List<ContactGroupResponseDto> toContactGroupResponseDtos(List<ContactGroup> contactGroups);

}
