//define classes 
package com.crimealert.CrimeAlert.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "crimes")
public class CrimeModel {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    public String description;

    @DatabaseField
    private float longitude;

    @DatabaseField
    private float latitude;

    private String address;

    public CrimeModel() {}

    public CrimeModel(float longitude, float latitude, String description) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
