package com.StudyCafe_R.modules.zone;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone,Long> {

    Zone findByCityAndProvince(String cityName, String provinceName);
}
