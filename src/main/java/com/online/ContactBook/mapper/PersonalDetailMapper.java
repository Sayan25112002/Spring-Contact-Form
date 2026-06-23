package com.online.ContactBook.mapper;

import com.online.ContactBook.dto.requestDto.PersonalDetailRequestDto;
import com.online.ContactBook.dto.responseDto.PersonalDetailResponseDto;
import com.online.ContactBook.entity.PersonalDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = ContactGroupMapper.class
)
public interface PersonalDetailMapper {

    PersonalDetail toPersonalDetail(PersonalDetailRequestDto personalDetailRequestDto);

    @Mapping(source = "contactGroups", target = "contactGroupResponseDtoList")
    PersonalDetailResponseDto toPersonalDetailResponseDto(PersonalDetail personalDetail);

    @Mapping(source = "contactGroups", target = "contactGroupResponseDtoList")
    List<PersonalDetailResponseDto> toPersonalDetailResponseDtos(List<PersonalDetail> personalDetails);

}
