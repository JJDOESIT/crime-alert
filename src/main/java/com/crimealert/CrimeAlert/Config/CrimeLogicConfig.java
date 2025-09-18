package com.crimealert.CrimeAlert.Config;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.crimealert.CrimeAlert.Logic.CrimeLogic;

@Configuration
public class CrimeLogicConfig {
    @Bean
    public CrimeLogic crimeLogic(HttpClient httpClient) {
        return new CrimeLogic(httpClient);
    }
}
