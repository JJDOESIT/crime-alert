package com.crimealert.CrimeAlert;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.util.ArrayList;
import java.util.HashMap;

import com.crimealert.CrimeAlert.Model.CrimeModel;
import com.crimealert.CrimeAlert.Model.GeolocationModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Entry {
    public static void main(String[] args) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("description", "Armed robber!");
            String data = mapper.writeValueAsString(map);
            int radius = 50;

            HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:8080/api/crime/?radius=" + radius))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            // HttpRequest request = HttpRequest.newBuilder(new
            // URI("http://localhost:8080/api/crime/"))
            // .header("Content-Type", "application/json")
            // .POST(HttpRequest.BodyPublishers.ofString(data))
            // .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ArrayList<CrimeModel> crimes = mapper.readValue(response.body(),
                    new TypeReference<ArrayList<CrimeModel>>() {
                    });
            System.out.println("Finished");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
