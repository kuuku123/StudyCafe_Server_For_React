package com.StudyCafe_R.infra.util;

import com.StudyCafe_R.modules.tag.TagService;
import com.StudyCafe_R.modules.zone.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MultipleTaskCommandLineRunner implements CommandLineRunner {

    private final DatabaseTruncator databaseTruncator;
    private final ZoneService zoneService;
    private final TagService tagService;


    @Override
    public void run(String... args) throws Exception {
        databaseTruncator.truncateAllTables();
        zoneService.initZoneData();
        tagService.initTagData();
    }
}
