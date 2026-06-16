package com.online.ContactBook.mapper;

import com.online.ContactBook.dto.responseDto.ContactGroupResponseDto;
import com.online.ContactBook.entity.ContactGroup;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactGroupMapper {

    ContactGroup toContactGroup(ContactGroup contactGroup);

    ContactGroupResponseDto toContactGroupResponseDto(ContactGroup contactGroup);

    List<ContactGroupResponseDto> toContactGroupResponseDtos(List<ContactGroup> contactGroups);

}
