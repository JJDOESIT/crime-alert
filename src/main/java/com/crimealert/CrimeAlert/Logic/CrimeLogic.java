package com.crimealert.CrimeAlert.Logic;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CrimeLogic {

    private final HttpClient _httpClient;

    public CrimeLogic(HttpClient httpClient) {
        _httpClient = httpClient;
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
