package com.crimealert.CrimeAlert.Logic;

import com.crimealert.CrimeAlert.Model.CrimeModel;
import com.crimealert.CrimeAlert.Model.GeolocationModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CrimeLogic {

    private final HttpClient _httpClient;
    private final Dao<CrimeModel, Integer> _userDao;

    public CrimeLogic(HttpClient httpClient, Dao<CrimeModel, Integer> daoConfig) {
        _httpClient = httpClient;
        _userDao = daoConfig;
    }

    // Fetch all crimes within a given radius
    public ArrayList<CrimeModel> getCrimes(int radius) {
        ArrayList<CrimeModel> filteredCrimes = new ArrayList<>();

        try {
            String ip = getIpAddress();
            GeolocationModel geolocation = getGeolocationInfoFromIpAddress(ip);

            List<CrimeModel> data = _userDao.queryForAll();

            for (CrimeModel model : data) {
                double distance = calculateDistance(model.latitude, model.longitude, geolocation.lat, geolocation.lon);
                if (distance <= radius) {
                    filteredCrimes.add(model);
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        return filteredCrimes;
    }

    // Insert a crime into the database
    public void postCrime(String description) {
        try {
            String ip = getIpAddress();
            GeolocationModel geolocation = getGeolocationInfoFromIpAddress(ip);
            CrimeModel model = new CrimeModel(description, geolocation.lat, geolocation.lon, geolocation.country,
                    geolocation.regionName, geolocation.city);
            _userDao.create(model);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    // Fetch a users ip address using ipify api
    private String getIpAddress() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(new URI("https://api.ipify.org/"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Fetch geolocation data given an ip address
    private GeolocationModel getGeolocationInfoFromIpAddress(String ipAddress)
            throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(new URI("http://ip-api.com/json/" + ipAddress))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        GeolocationModel info = mapper.readValue(response.body(), GeolocationModel.class);
        return info;
    }

    // Calculate distance between two coordinates
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS = 3958.8; // miles

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
