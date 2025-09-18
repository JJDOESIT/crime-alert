package com.crimealert.CrimeAlert.Logic;

import com.crimealert.CrimeAlert.Model.CrimeModel;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CrimeLogic {

    private final HttpClient _httpClient;
    private final Dao<CrimeModel, Integer> _userDao;

    public CrimeLogic(HttpClient httpClient, Dao<CrimeModel, Integer> daoConfig) {
        _httpClient = httpClient;
        _userDao = daoConfig;
    }

    private String getIpAddress() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(new URI("https://api.ipify.org/"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
