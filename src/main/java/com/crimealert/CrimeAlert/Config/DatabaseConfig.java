package com.crimealert.CrimeAlert.Config;

import com.crimealert.CrimeAlert.Model.CrimeModel;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
    private static final String DATABASE_URL = "jdbc:sqlite:crimes.db";

    @Bean
    public ConnectionSource connectionSource() throws Exception {
        ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
        TableUtils.createTableIfNotExists(connectionSource, CrimeModel.class);
        return connectionSource;
    }
}
