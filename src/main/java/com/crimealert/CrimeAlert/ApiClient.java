package com.crimealert.CrimeAlert;

import com.crimealert.CrimeAlert.Model.CrimeModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient client;
    private final ObjectMapper mapper;

    public ApiClient() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public void launchAPI() {
        new Thread(() -> {
            CrimeAlertApplication.main(new String[] {});
        }).start();

        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CrimeModel> getCrimes(int radius) throws Exception {
        URI uri = new URI(BASE_URL + "/crime/?radius=" + radius);

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return mapper.readValue(response.body(),
                new TypeReference<ArrayList<CrimeModel>>() {
                });
    }

    public void postCrime(String description) throws Exception {
        URI uri = new URI(BASE_URL + "/crime/");

        // Build JSON body
        String json = mapper.writeValueAsString(Map.of(
                "description", description));

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
