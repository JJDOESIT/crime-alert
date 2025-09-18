package com.crimealert.CrimeAlert.Config;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.crimealert.CrimeAlert.Logic.CrimeLogic;
import com.crimealert.CrimeAlert.Model.CrimeModel;
import com.j256.ormlite.dao.Dao;

@Configuration
public class CrimeLogicConfig {
    @Bean
    public CrimeLogic crimeLogic(HttpClient httpClient, Dao<CrimeModel, Integer> daoConfig) {
        return new CrimeLogic(httpClient, daoConfig);
    }
}
