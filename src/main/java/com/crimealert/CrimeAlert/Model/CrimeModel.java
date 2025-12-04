package com.crimealert.CrimeAlert.Model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "crimes")
public class CrimeModel {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String description;

    @DatabaseField
    public float latitude;

    @DatabaseField
    public float longitude;

    @DatabaseField
    public String country;

    @DatabaseField
    public String regionName;

    @DatabaseField
    public String city;

    @DatabaseField
    public Date timestamp;

    public CrimeModel() {

    }
    @JsonIgnore
    public String getAddressString() {return String.format("%s, %s, %s", this.regionName, this.city, this.country);}

    public CrimeModel(String description, float latitude, float longitude, String country, String regionName,
            String city) {
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.regionName = regionName;
        this.city = city;
        this.timestamp = new Date();
    }
}
