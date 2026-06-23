package com.online.ContactBook.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactGroupRequestDto {

    @NotBlank(message = "Group Name is needed")
    private String groupName;

}
