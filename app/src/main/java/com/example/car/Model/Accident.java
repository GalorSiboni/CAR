package com.example.car.Model;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Accident {

    private Date date = new Date();
    private String openDate;
    private ArrayList<Profile> profiles = new ArrayList<>(  );
    private LatLng location;
   // private double latitude,longitude;

    public Accident(double latitude, double longitude)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.openDate = dateFormat.format(this.date);
        this.location = new LatLng( latitude, longitude);
    }
    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public ArrayList<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<Profile> profiles) {
        this.profiles = profiles;
    }

    public void addToProfilesList(Profile profile) {
        this.profiles.add( profile );
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

}
