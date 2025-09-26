package com.rh.api_rh;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Profile("!test")  // habilita só para perfis diferentes de test
@EnableScheduling
public class SchedulingConfig {
}