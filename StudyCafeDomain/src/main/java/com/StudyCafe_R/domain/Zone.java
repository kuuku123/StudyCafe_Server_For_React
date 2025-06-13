package com.StudyCafe_R.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Zone {

    private Long id;

    private String city;
    private String localNameOfCity;
    private String province;

    private Set<Long> accounts = new HashSet<>();
    private Set<Long> studies = new HashSet<>();

    @Builder
    public Zone(Long id, String city, String localNameOfCity, String province) {
        this.id = id;
        this.city = city;
        this.localNameOfCity = localNameOfCity;
        this.province = province;
    }
}
