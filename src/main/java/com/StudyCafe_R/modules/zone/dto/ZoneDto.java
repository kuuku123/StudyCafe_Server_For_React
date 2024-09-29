package com.StudyCafe_R.modules.zone.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ZoneDto {
    private Long id;
    private String city;
    private String localNameOfCity;
    private String province;
}
