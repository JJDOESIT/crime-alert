package com.crimealert.CrimeAlert;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Entry {
    public static void main(String[] args) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:8080/api/crime/"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
