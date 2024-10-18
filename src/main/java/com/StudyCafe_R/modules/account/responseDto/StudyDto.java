package com.StudyCafe_R.modules.account.responseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudyDto {

    private String title;
    private String path;
    private String shortDescription;
    private String fullDescription;
    private String studyImage;
    private boolean published;
}
