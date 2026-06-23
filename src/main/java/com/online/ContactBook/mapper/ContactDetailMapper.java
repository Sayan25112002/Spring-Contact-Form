package com.online.ContactBook.mapper;

import com.online.ContactBook.dto.requestDto.ContactDetailRequestDto;
import com.online.ContactBook.dto.responseDto.ContactDetailResponseDto;
import com.online.ContactBook.entity.ContactDetail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface ContactDetailMapper {

    ContactDetail toContactDetail(ContactDetailRequestDto contactDetailRequestDto);

    ContactDetailResponseDto toContactDetailResponseDto(ContactDetail contactDetail);

    List<ContactDetailResponseDto> toContactDetailResponseDtos(List<ContactDetail> contactDetails);

}
