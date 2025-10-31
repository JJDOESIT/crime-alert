//have two functions for getting and inserting
//helper functions as well (ip address)

//function for haversign formula. take in distance, lat and long between two points
    //returns true false if points are between this range


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
    
        //radius in km
        private static final int EARTH_RADIUS = 6371;

        public double calcluateDistance(double lat1, double lat2, double lon1, double lon2)
        {
            //degrees to radians
            double dLat = Math.toRadians(lat2-lat1);
            double dLon = Math.toRadians(lon2-lon1);

            lat1 = Math.toRadians(lat1);
            lat2 = Math.toRadians(lat2);


            //haversine formula
            double a = Math.pow(Math.sin(dLat / 2), 2) +
                       Math.cos(lat1) * Math.cos(lat2) *
                       Math.pow(Math.sin(dLon / 2), 2);
                       
            //angular distance
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

            return EARTH_RADIUS * c;

        }




}


