package com.StudyCafe_R.modules.zone.dto;

import jakarta.persistence.Column;
import lombok.Data;

public class ZoneDto {
    private Long id;
    private String city;
    private String localNameOfCity;
    private String province;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocalNameOfCity() {
        return localNameOfCity;
    }

    public void setLocalNameOfCity(String localNameOfCity) {
        this.localNameOfCity = localNameOfCity;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
