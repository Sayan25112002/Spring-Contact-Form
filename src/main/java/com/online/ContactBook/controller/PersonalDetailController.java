package com.online.ContactBook.controller;

import com.online.ContactBook.dto.requestDto.ContactDetailRequestDto;
import com.online.ContactBook.dto.requestDto.ContactGroupRequestDto;
import com.online.ContactBook.dto.requestDto.ContactRequestDto;
import com.online.ContactBook.dto.requestDto.PersonalDetailRequestDto;
import com.online.ContactBook.dto.responseDto.ContactDetailResponseDto;
import com.online.ContactBook.dto.responseDto.ContactGroupResponseDto;
import com.online.ContactBook.dto.responseDto.ContactResponseDto;
import com.online.ContactBook.dto.responseDto.PersonalDetailResponseDto;
import com.online.ContactBook.entity.PersonalDetail;
import com.online.ContactBook.repository.PersonalDetailRepository;
import com.online.ContactBook.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class PersonalDetailController {

    private final ContactService contactService;

    @PostMapping("/createPersonalDetail/{memberId}")
    public PersonalDetailResponseDto createPersonalDetail(@RequestBody PersonalDetailRequestDto personalDetailRequestDto, @PathVariable Long memberId) {
        return contactService.createPersonalDetail(personalDetailRequestDto,memberId);
    }

    @PostMapping("/createContactGroup/{personalDetailId}")
    public ContactGroupResponseDto createContactGroup(@Valid @RequestBody ContactGroupRequestDto contactGroupRequestDto, @PathVariable Long personalDetailId) {
        return contactService.createContactGroup(contactGroupRequestDto,personalDetailId);
    }

    @PostMapping("/createContact/{contactGroupIds}")
    public ContactResponseDto createContact(@Valid @ModelAttribute ContactRequestDto contactRequestDto, @PathVariable List<Long> contactGroupIds) throws IOException {
        return contactService.createContact(contactRequestDto,contactGroupIds);
    }

    @PostMapping("/createContactDetails/{contactId}")
    public ContactDetailResponseDto createContactDetails(@Valid @RequestBody ContactDetailRequestDto contactDetailRequestDto, @PathVariable Long contactId) {
        return contactService.createContactDetail(contactDetailRequestDto,contactId);
    }

    @GetMapping("/getPersonalDetail/{id}")
    public PersonalDetailResponseDto getPersonalDetail(@PathVariable Long id) {
        return contactService.getPersonalDetail(id);
    }

    @PatchMapping("/updatePersonalDetail/{id}")
    public PersonalDetailResponseDto updatePersonalDetail(@RequestBody PersonalDetailRequestDto personalDetailRequestDto, @PathVariable Long id) {
        return contactService.updatePersonalDetail(personalDetailRequestDto,id);
    }

    @PatchMapping("/updateContactGroup/{id}")
    public ContactGroupResponseDto updateContactGroup(@RequestBody ContactGroupRequestDto contactGroupRequestDto, @PathVariable Long id) {
        return contactService.updateContactGroup(contactGroupRequestDto,id);
    }

    @PatchMapping("/updateContact/{id}")
    public ContactResponseDto updateContact(@ModelAttribute ContactRequestDto contactRequestDto, @PathVariable Long id) throws IOException {
        return contactService.updateContact(contactRequestDto,id);
    }

    @PatchMapping("/updateContactDetail/{id}")
    public ContactDetailResponseDto updateContactDetail(@RequestBody ContactDetailRequestDto contactDetailRequestDto, @PathVariable Long id) {
        return contactService.updateContactDetail(contactDetailRequestDto,id);
    }

    @DeleteMapping("/deletePersonalDetail/{id}")
    public void deletePersonalDetail(@PathVariable Long id) {
        contactService.deletePersonalDetail(id);
        System.out.println("Personal detail deleted successfully");
    }

    @DeleteMapping("/deleteContactGroup/{id}")
    public void deleteContactGroup(@PathVariable Long id) {
            contactService.deleteContactGroupById(id);
            System.out.println("Delete contact group successfully");
    }

    @DeleteMapping("/deleteContact/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactService.deleteContactById(id);
        System.out.println("Delete contact successfully");
    }

    @DeleteMapping("/deleteContactDetail/{id}")
    public void deleteContactDetail(@PathVariable Long id) {
        contactService.deleteContactDetailById(id);
        System.out.println("Delete contact detail successfully");
    }

}
