package com.online.ContactBook.service;

import com.online.ContactBook.dto.requestDto.ContactDetailRequestDto;
import com.online.ContactBook.dto.requestDto.ContactGroupRequestDto;
import com.online.ContactBook.dto.requestDto.ContactRequestDto;
import com.online.ContactBook.dto.requestDto.PersonalDetailRequestDto;
import com.online.ContactBook.dto.responseDto.ContactDetailResponseDto;
import com.online.ContactBook.dto.responseDto.ContactGroupResponseDto;
import com.online.ContactBook.dto.responseDto.ContactResponseDto;
import com.online.ContactBook.dto.responseDto.PersonalDetailResponseDto;
import com.online.ContactBook.entity.Contact;
import com.online.ContactBook.entity.ContactDetail;
import com.online.ContactBook.entity.ContactGroup;

import java.io.IOException;
import java.util.List;

public interface ContactService {

    PersonalDetailResponseDto createPersonalDetail(PersonalDetailRequestDto personalDetailRequestDto, Long memberId);

    ContactGroupResponseDto createContactGroup(ContactGroupRequestDto contactGroupRequestDto, Long personalDetailId);

    ContactResponseDto createContact(ContactRequestDto contactRequestDto, List<Long> contactGroupIds) throws IOException;

    ContactDetailResponseDto createContactDetail(ContactDetailRequestDto contactDetailRequestDto, Long contactId);

    PersonalDetailResponseDto getPersonalDetail(Long id);

    PersonalDetailResponseDto updatePersonalDetail(PersonalDetailRequestDto personalDetailRequestDto, Long id);

    ContactGroupResponseDto updateContactGroup(ContactGroupRequestDto contactGroupRequestDto, Long id);

    ContactResponseDto updateContact(ContactRequestDto contactRequestDto, Long id) throws IOException;

    ContactDetailResponseDto updateContactDetail(ContactDetailRequestDto contactDetailRequestDto, Long id);

    void deletePersonalDetail(Long id);

    void deleteContactGroupById(Long id);

    void deleteContactById(Long id);

    void deleteContactDetailById(Long id);

}
