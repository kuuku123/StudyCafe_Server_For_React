package com.StudyCafe_R.modules.study.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class StudyForm {


    @NotBlank
    @Length(min = 2, max = 20)
    private String path;

    @NotBlank
    @Length(max = 50)
    private String title;

    @NotBlank
    @Length(max = 100)
    private String shortDescription;

    @NotBlank
    private String fullDescription;

    @NotBlank
    private String fullDescriptionText;

    private String studyImage;
}
