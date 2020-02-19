package com.example.car.Model;

import java.util.Date;

public class Profile extends User{
    private String carNumber = "";//done
    private String carModel = "";//done
    private String carColor = "";//done
    private String driverName = "";//done
    private String id = "";//done
    private String address = "";//done
    private String licenceNumber = "";//done
    private String phoneNumber = "";//done
    private String ownerAddress = "";//done
    private String ownerPhoneNumber = "";//done
    private String insuranceCompanyName = "";//done
    private String insurancePolicyNumber = "";//done
    private String insuranceAgentName = "";//done
    private String insuranceAgentPhoneNum = "";

    public Profile(String username,String password,String mail,String fName, String sName, String carNumber, String carModel, String carColor, String driverName, String id, String address, String licenceNumber, String phoneNumber, String ownerAddress, String ownerPhoneNumber, String insuranceCompanyName, String insurancePolicyNumber, String insuranceAgentName, String insuranceAgentPhoneNum) {
        super(username, password, fName, sName, mail);
        setFirstName( fName );
        setLastName( sName );
        this.carNumber = carNumber;
        this.carModel = carModel;
        this.carColor = carColor;
        this.driverName = driverName;
        this.id = id;
        this.address = address;
        this.licenceNumber = licenceNumber;
        this.phoneNumber = phoneNumber;
        this.ownerAddress = ownerAddress;
        this.ownerPhoneNumber = ownerPhoneNumber;
        this.insuranceCompanyName = insuranceCompanyName;
        this.insurancePolicyNumber = insurancePolicyNumber;
        this.insuranceAgentName = insuranceAgentName;
        this.insuranceAgentPhoneNum = insuranceAgentPhoneNum;
    }
    public Profile(){}
    public Profile(String username, String password, String fName, String sName, String mail)
    {
        super(username, password, fName, sName, mail);
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;//maybe to do strdup or copy in order to create new string in the memory
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public void setOwnerPhoneNumber(String ownerPhoneNumber) {
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public void setInsurancePolicyNumber(String insurancePolicyNumber) {
        this.insurancePolicyNumber = insurancePolicyNumber;
    }

    public void setInsuranceAgentName(String insuranceAgentName) {
        this.insuranceAgentName = insuranceAgentName;
    }

    public void setInsuranceAgentPhoneNum(String insuranceAgentPhoneNum) {
        this.insuranceAgentPhoneNum = insuranceAgentPhoneNum;
    }
    public String getCarNumber() {
        return carNumber;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public String getOwnerPhoneNumber() {
        return ownerPhoneNumber;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public String getInsurancePolicyNumber() {
        return insurancePolicyNumber;
    }

    public String getInsuranceAgentName() {
        return insuranceAgentName;
    }

    public String getInsuranceAgentPhoneNum() {
        return insuranceAgentPhoneNum;
    }
}
