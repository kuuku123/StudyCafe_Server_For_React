package com.StudyCafe_R.modules.zone;


import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final ModelMapper mapper;

    public List<ZoneDto> getAllZone() {
        List<Zone> zones = zoneRepository.findAll();
        ArrayList<ZoneDto> zoneDtos = new ArrayList<>();
        for (Zone zone : zones) {
            ZoneDto map = mapper.map(zone, ZoneDto.class);
            zoneDtos.add(map);
        }
        return zoneDtos;
    }

    public Zone findByCityAndProvince (String city, String province) {
        Zone byCityAndProvince = zoneRepository.findByCityAndProvince(city, province);
        return byCityAndProvince;
    }

    public void initZoneData() throws IOException {
        if (zoneRepository.count() == 0) {
            Resource resource = new ClassPathResource("zones_kr.csv");

            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < 85) {
                sb.append(br.readLine());
                sb.append("\n");
                i++;
            }
            String s = sb.toString();
            String[] zones = s.split("\n");

            List<Zone> zoneList = Arrays.stream(zones).map(line -> {
                String[] split = line.split(",");
                return Zone.builder()
                        .city(split[0])
                        .localNameOfCity(split[1])
                        .province(split[2])
                        .build();
            }).collect(Collectors.toList());
            zoneRepository.saveAll(zoneList);
        }
    }
}
