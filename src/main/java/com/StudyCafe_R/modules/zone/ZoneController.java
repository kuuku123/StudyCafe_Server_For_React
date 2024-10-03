package com.StudyCafe_R.modules.zone;

import com.StudyCafe_R.infra.config.converter.LocalDateTimeAdapter;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.zone.dto.ZoneDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @GetMapping("/get-all-zones")
    public ResponseEntity<String> getZones() {
        List<ZoneDto> zones = zoneService.getAllZone();
        ApiResponse<List<ZoneDto>> apiResponse = new ApiResponse<>("zones", HttpStatus.OK, zones);
        return new ResponseEntity<>(gson.toJson(apiResponse), HttpStatus.OK);
    }
}
