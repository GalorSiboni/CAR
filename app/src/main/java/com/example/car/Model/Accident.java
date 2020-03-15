package com.example.car.Model;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
public class Accident {

    private static int counter = 0;//if the same two drivers had more then one accident -> the counter different between the cases
    private String accidentId;
    private Date date;
    private String locationStr;
    private String openDate;
//    private LatLng location;
    private Profile driverThatScan;
    private Profile driverWhoGotScanned;

    public void setLocationStr(String locationStr) {
        this.locationStr = locationStr;
    }

    public String getAccidentId() {
        return accidentId;
    }

    public Accident(Profile driverThatScan, Profile driverWhoGotScanned)
    {
        this.driverThatScan = driverThatScan;
        this.driverWhoGotScanned = driverWhoGotScanned;
        counter++;
        this.accidentId = counter + driverThatScan.getUsername() + "_" + driverWhoGotScanned.getUsername();
        this.date = Calendar.getInstance().getTime();
        this.openDate = DateFormat.getDateInstance().format(date);
        this.locationStr = "";
//        this.location = new LatLng(latitude, longitude);
    }

    public Accident()
    {
        this.driverThatScan = new Profile();
        this.driverWhoGotScanned = new Profile();
        this.date = Calendar.getInstance().getTime();
        this.openDate = DateFormat.getDateInstance().format(date);
//        this.location = new LatLng(0,0) ;
        this.locationStr = "";
    }
    public Profile getDriverThatScan() {
        return driverThatScan;
    }

    public void setDriverThatScan(Profile driverThatScan) {
        this.driverThatScan = driverThatScan;
    }

    public Profile getDriverWhoGotScanned() {
        return driverWhoGotScanned;
    }

    public void setDriverWhoGotScanned(Profile driverWhoGotScanned) {
        this.driverWhoGotScanned = driverWhoGotScanned;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

//    public LatLng getLocation() {
//        return location;
//    }

//    public void setLocation(LatLng location) {
//        this.location = location;
//    }

    public String getLocationStr() {
        return locationStr;
    }
}
