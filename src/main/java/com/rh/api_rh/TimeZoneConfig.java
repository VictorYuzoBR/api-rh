package com.rh.api_rh;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.TimeZone;

@Component
public class TimeZoneConfig {

    @PostConstruct
    public void init() {

        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        System.out.println(">>> Timezone global definido: " + ZoneId.systemDefault());

    }

}
