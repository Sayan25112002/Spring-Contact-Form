package com.online.ContactBook.dto.requestDto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactRequestDto {

    private String name;

    private String image;

    private MultipartFile imageFile;

}
