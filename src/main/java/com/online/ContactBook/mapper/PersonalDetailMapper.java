package com.online.ContactBook.mapper;

import com.online.ContactBook.dto.requestDto.PersonalDetailRequestDto;
import com.online.ContactBook.dto.responseDto.PersonalDetailResponseDto;
import com.online.ContactBook.entity.PersonalDetail;
import jakarta.persistence.ManyToOne;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonalDetailMapper {

    PersonalDetail toPersonalDetail(PersonalDetailRequestDto personalDetailRequestDto);

    PersonalDetailResponseDto toPersonalDetailResponseDto(PersonalDetail personalDetail);

    List<PersonalDetailResponseDto> toPersonalDetailResponseDtos(List<PersonalDetail> personalDetails);

}
