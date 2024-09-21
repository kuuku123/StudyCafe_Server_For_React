package com.StudyCafe_R.modules.account.responseDto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountStudyManagerDto {

    private String title;
    private String path;
    private String shortDescription;
    private String fullDescription;
    private String studyImage;
}
