package com.crimealert.CrimeAlert.Config;

import com.crimealert.CrimeAlert.Model.CrimeModel;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {

    @Bean
    public Dao<CrimeModel, Integer> userDao(ConnectionSource connectionSource) throws Exception {
        return DaoManager.createDao(connectionSource, CrimeModel.class);
    }
}