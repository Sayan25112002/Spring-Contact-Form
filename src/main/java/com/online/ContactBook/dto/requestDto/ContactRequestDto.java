package com.online.ContactBook.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ContactRequestDto {

    private String name;

    private String image;

    private MultipartFile imageFile;

}
