package com.online.ContactBook.service.implementation;

import com.online.ContactBook.dto.requestDto.ContactDetailRequestDto;
import com.online.ContactBook.dto.requestDto.ContactGroupRequestDto;
import com.online.ContactBook.dto.requestDto.ContactRequestDto;
import com.online.ContactBook.dto.requestDto.PersonalDetailRequestDto;
import com.online.ContactBook.dto.responseDto.ContactDetailResponseDto;
import com.online.ContactBook.dto.responseDto.ContactGroupResponseDto;
import com.online.ContactBook.dto.responseDto.ContactResponseDto;
import com.online.ContactBook.dto.responseDto.PersonalDetailResponseDto;
import com.online.ContactBook.entity.*;
import com.online.ContactBook.mapper.ContactDetailMapper;
import com.online.ContactBook.mapper.ContactGroupMapper;
import com.online.ContactBook.mapper.ContactMapper;
import com.online.ContactBook.mapper.PersonalDetailMapper;
import com.online.ContactBook.repository.*;
import com.online.ContactBook.service.ContactService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final MemberRepository memberRepository;
    private final PersonalDetailRepository personalDetailRepository;
    private final ContactRepository contactRepository;
    private final ContactGroupRepository contactGroupRepository;
    private final ContactDetailRepository contactDetailRepository;
    private final PersonalDetailMapper  personalDetailMapper;
    private final ContactMapper contactMapper;
    private final ContactGroupMapper contactGroupMapper;
    private final ContactDetailMapper contactDetailMapper;


    @Override
    public PersonalDetailResponseDto createPersonalDetail(PersonalDetailRequestDto personalDetailRequestDto, Long memberId) {
        PersonalDetail personalDetail = personalDetailMapper.toPersonalDetail(personalDetailRequestDto);
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new EntityNotFoundException("Member Not Found"));
        PersonalDetail personalDetail1 = PersonalDetail.builder()
                .firstName(member.getFirstName())
                .middleName(member.getMiddleName())
                .lastName(member.getLastName())
                .email(member.getUsername())
                .phone(member.getPhone())
                .fatherName(personalDetail.getFatherName())
                .motherName(personalDetail.getMotherName())
                .address(personalDetail.getAddress())
                .member(member)
                .build();
        PersonalDetail savedPersonalDetail = personalDetailRepository.save(personalDetail1);
        return personalDetailMapper.toPersonalDetailResponseDto(savedPersonalDetail);
    }

    @Override
    public ContactGroupResponseDto createContactGroup(ContactGroupRequestDto contactGroupRequestDto, Long personalDetailId) {
        ContactGroup contactGroup = contactGroupMapper.toContactGroup(contactGroupRequestDto);
        PersonalDetail personalDetail = personalDetailRepository.findById(personalDetailId).orElseThrow(()-> new EntityNotFoundException("Member Not Found"));
        contactGroup.setPersonalDetail(personalDetail);
        personalDetail.getContactGroups().add(contactGroup);
        ContactGroup savedContactGroup = contactGroupRepository.save(contactGroup);
        return contactGroupMapper.toContactGroupResponseDto(savedContactGroup);
    }

    @Override
    public ContactResponseDto createContact(ContactRequestDto contactRequestDto, List<Long> contactGroupIds) throws IOException {
        ContactRequestDto contactRequestDto1 = ContactRequestDto.builder()
                .name(contactRequestDto.getName())
                .image(saveImage(contactRequestDto.getImageFile()))
                .build();
        Contact contact = contactMapper.toContact(contactRequestDto1);
        List<ContactGroup> contactGroups = contactGroupRepository.findAllById(contactGroupIds);
        contact.setContactGroups(contactGroups);
        Contact savedContact = contactRepository.save(contact);
        return contactMapper.toContactResponseDto(savedContact);
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        String uploadDir = System.getProperty("user.dir")+"\\src\\main\\resources\\webapp\\images";
        Files.createDirectories(Paths.get(uploadDir));
        String imageFilePath = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path path = Paths.get(uploadDir, imageFilePath);
        Files.write(path, imageFile.getBytes());
        return path.toString();
    }

    @Override
    public ContactDetailResponseDto createContactDetail(ContactDetailRequestDto contactDetailRequestDto, Long contactId) {
        ContactDetail contactDetail = contactDetailMapper.toContactDetail(contactDetailRequestDto);
        Contact contact = contactRepository.findById(contactId).orElseThrow(()-> new EntityNotFoundException("Contact Not Found"));
        contactDetail.setContact(contact);
        contact.getContactDetails().add(contactDetail);
        ContactDetail savedContactDetail = contactDetailRepository.save(contactDetail);
        return contactDetailMapper.toContactDetailResponseDto(savedContactDetail);
    }

    @Override
    public PersonalDetailResponseDto getPersonalDetail(Long id) {
        PersonalDetail personalDetail = personalDetailRepository.getById(id);
        return personalDetailMapper.toPersonalDetailResponseDto(personalDetail);
    }

    @Override
    public PersonalDetailResponseDto updatePersonalDetail(PersonalDetailRequestDto personalDetailRequestDto, Long id) {
        PersonalDetail personalDetail = personalDetailRepository.getById(id);
        if(personalDetailRequestDto.getFatherName()!=null){
            personalDetail.setFatherName(personalDetailRequestDto.getFatherName());
        }
        if(personalDetailRequestDto.getMotherName()!=null){
            personalDetail.setMotherName(personalDetailRequestDto.getMotherName());
        }
        if(personalDetailRequestDto.getAddress()!=null){
            personalDetail.setAddress(personalDetailRequestDto.getAddress());
        }
        PersonalDetail savedPersonalDetail = personalDetailRepository.save(personalDetail);
        return personalDetailMapper.toPersonalDetailResponseDto(savedPersonalDetail);
    }

    @Override
    public ContactGroupResponseDto updateContactGroup(ContactGroupRequestDto contactGroupRequestDto, Long id) {
        ContactGroup contactGroup = contactGroupRepository.getById(id);
        if(contactGroupRequestDto.getGroupName()!=null){
            contactGroup.setGroupName(contactGroupRequestDto.getGroupName());
        }
        ContactGroup savedContactGroup = contactGroupRepository.save(contactGroup);
        return contactGroupMapper.toContactGroupResponseDto(savedContactGroup);
    }

    @Override
    public ContactResponseDto updateContact(ContactRequestDto contactRequestDto, Long id) throws IOException {
        Contact contact = contactRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Contact Not Found"));
        if(contactRequestDto.getName()!=null){
            contact.setName(contactRequestDto.getName());
        }
        if(contactRequestDto.getImageFile()!=null){
            contact.setImage(saveImage(contactRequestDto.getImageFile()));
        }
        Contact savedContact = contactRepository.save(contact);
        return contactMapper.toContactResponseDto(savedContact);
    }

    @Override
    public ContactDetailResponseDto updateContactDetail(ContactDetailRequestDto contactDetailRequestDto, Long id) {
        ContactDetail contactDetail = contactDetailRepository.getById(id);
        if(contactDetailRequestDto.getContactType()!=null){
            contactDetail.setContactType(contactDetailRequestDto.getContactType());
        }
        if(contactDetailRequestDto.getAddress() !=null){
            contactDetail.setAddress(contactDetailRequestDto.getAddress());
        }
        if(contactDetailRequestDto.getMobile()!=null){
            contactDetail.setMobile(contactDetailRequestDto.getMobile());
        }
        if(contactDetailRequestDto.getEmail()!=null){
            contactDetail.setEmail(contactDetailRequestDto.getEmail());
        }
        if(contactDetailRequestDto.getPhone()!=null){
            contactDetail.setPhone(contactDetailRequestDto.getPhone());
        }
        if (contactDetailRequestDto.getWebsite()!=null){
            contactDetail.setWebsite(contactDetailRequestDto.getWebsite());
        }
        ContactDetail savedContactDetail = contactDetailRepository.save(contactDetail);
        return contactDetailMapper.toContactDetailResponseDto(savedContactDetail);
    }

    @Override
    public void deletePersonalDetail(Long id) {
        PersonalDetail personalDetail = personalDetailRepository.findById(id).orElseThrow(()->
                new EntityNotFoundException("Personal Detail Not Found"));
        List<ContactGroup> contactGroups = new ArrayList<>(personalDetail.getContactGroups());
        for (ContactGroup contactGroup : contactGroups) {
            deleteContactGroupById(contactGroup.getId());
        }
        Member member = personalDetail.getMember();
        if (member != null) {
            member.setPersonalDetail(null);
        }
        personalDetail.setMember(null);
        personalDetailRepository.delete(personalDetail);
    }

    @Override
    public void deleteContactGroupById(Long id) {
        ContactGroup contactGroup = contactGroupRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Contact Group Not Found"));
        List<Contact> contacts = new ArrayList<>(contactGroup.getContacts());
        for (Contact contact : contacts) {
            deleteContactById(contact.getId());
        }
        contactGroupRepository.delete(contactGroup);
    }

    @Override
    public void deleteContactById(Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Contact Not Found"));
        contactRepository.delete(contact);
    }


    @Override
    public void deleteContactDetailById(Long id) {
        if(contactDetailRepository.existsById(id)){
            contactDetailRepository.deleteById(id);
        }
        else{
            throw new EntityNotFoundException("Contact Detail Not Found");
        }
    }
}
