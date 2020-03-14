package com.example.car.Model;

import com.example.car.Constants;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
public class Accident {

    private static int counter = 0;//if the same two drivers had more then one accident -> the counter different between the cases
    private String accidentId;
    private Date date;
    private String locationStr;

    public void setLocationStr(String locationStr) {
        this.locationStr = locationStr;
    }

    private String openDate;
    private LatLng location;
    private Profile driver1;
    private Profile driver2;

//    public Accident(double latitude, double longitude)
//    {
//        this.driver1 = driver1;
//        this.driver2 = driver2;
//        this.accidentId = driver1.getUsername() + "_" + driver2.getUsername();
//        this.date = Calendar.getInstance().getTime();
//        this.openDate = DateFormat.getDateInstance().format(date);
//        this.location = new LatLng(latitude, longitude);
//    }

    public String getAccidentId() {
        return accidentId;
    }

    public Accident()
    {
//        this.driver1 = driver1;
//        this.driver2 = driver2;
        counter++;
        this.accidentId = counter + driver1.getUsername() + "_" + driver2.getUsername();
        this.date = Calendar.getInstance().getTime();
        this.openDate = DateFormat.getDateInstance().format(date);
        this.locationStr = "";
//        this.location = new LatLng(latitude, longitude);
    }
    public Profile getDriver1() {
        return driver1;
    }

    public void setDriver1(Profile driver1) {
        this.driver1 = driver1;
    }

    public Profile getDriver2() {
        return driver2;
    }

    public void setDriver2(Profile driver2) {
        this.driver2 = driver2;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

//    public ArrayList<Profile> getProfiles() {
//        return profiles;
//    }
//
//    public void setProfiles(ArrayList<Profile> profiles) {
//        this.profiles = profiles;
//    }
//
//    public void addToProfilesList(Profile profile) {
//        this.profiles.add( profile );
//    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

}
