package com.example.car.Model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class Profile extends User {
    private String carNumber = "";
    private String carModel = "";
    private String carColor = "";
    private String driverName = "";
    private String id = "";
    private String address = "";
    private String licenceNumber = "";
    private String phoneNumber = "";
    private String ownerAddress = "";
    private String ownerPhoneNumber = "";
    private String insuranceCompanyName = "";
    private String insurancePolicyNumber = "";
    private String insuranceAgentName = "";
    private String insuranceAgentPhoneNum = "";
    private String imageUrl = "";

    public Profile(String username,String password,String mail,String fName, String sName, String carNumber, String carModel, String carColor, String driverName, String id, String address, String licenceNumber, String phoneNumber, String ownerAddress, String ownerPhoneNumber, String insuranceCompanyName, String insurancePolicyNumber, String insuranceAgentName, String insuranceAgentPhoneNum, String imageUrl) {
        super(username, password, fName, sName, mail);
        setFirstName(fName);
        setLastName(sName);
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
        this.imageUrl = imageUrl;
    }
    public Profile(){}
    public Profile(String username, String password, String fName, String sName, String mail)
    {
        super(username, password, fName, sName, mail);
    }

    public void updateProfile(String firstName, String lastName, String mail,String carNumber, String carModel, String carColor, String driverName, String id, String address, String licenceNumber, String phoneNumber, String ownerAddress, String ownerPhoneNumber, String insuranceCompanyName, String insurancePolicyNumber, String insuranceAgentName, String insuranceAgentPhoneNum, String imageUrl)
    {
        super.updateUser(firstName,lastName, mail);
        if(!this.carNumber.equals(carNumber))
            setCarNumber(carNumber);
        if(!this.carModel.equals(carModel))
            setCarModel(carModel);
        if(!this.driverName.equals(driverName))
            setDriverName(driverName);
        if(!this.id.equals(id))
            setId(id);
        if(!this.address.equals(address))
            setAddress(address);
        if(!this.licenceNumber.equals(licenceNumber))
            setLicenceNumber(licenceNumber);
        if(!this.phoneNumber.equals(phoneNumber))
            setPhoneNumber(phoneNumber);
        if(!this.ownerAddress.equals(ownerAddress))
            setOwnerAddress(ownerAddress);
        if(!this.carColor.equals(carColor))
            setCarColor(carColor);
        if(!this.ownerPhoneNumber.equals(ownerPhoneNumber))
            setOwnerPhoneNumber(ownerPhoneNumber);
        if(!this.insuranceAgentName.equals(insuranceAgentName))
        setInsuranceAgentName(insuranceAgentName);
        if(!this.insuranceAgentPhoneNum.equals(insuranceAgentPhoneNum))
            setInsuranceAgentPhoneNum(insuranceAgentPhoneNum);
        if(!this.insuranceCompanyName.equals(insuranceCompanyName))
            setInsuranceCompanyName(insuranceCompanyName);
        if(!this.insurancePolicyNumber.equals(insurancePolicyNumber))
            setInsurancePolicyNumber(insurancePolicyNumber);
        if(!this.imageUrl.equals(imageUrl))
            setImageUrl(imageUrl);

    }
    public void setCarNumber(String carNumber) {
        if(!this.carNumber.equals(carNumber))
            this.carNumber = carNumber;//maybe to do strdup or copy in order to create new string in the memory
    }

    public void setCarModel(String carModel) {
        if(!this.carModel.equals(carModel))
        this.carModel = carModel;
    }

    public void setCarColor(String carColor) {
        if(!this.carColor.equals(carColor))
            this.carColor = carColor;
    }

    public void setDriverName(String driverName) {
            if(!this.driverName.equals(driverName))
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Profile profile = (Profile) o;
        return carNumber.equals(profile.carNumber) &&
                carModel.equals(profile.carModel) &&
                carColor.equals(profile.carColor) &&
                driverName.equals(profile.driverName) &&
                id.equals(profile.id) &&
                address.equals(profile.address) &&
                licenceNumber.equals(profile.licenceNumber) &&
                phoneNumber.equals(profile.phoneNumber) &&
                ownerAddress.equals(profile.ownerAddress) &&
                ownerPhoneNumber.equals(profile.ownerPhoneNumber) &&
                insuranceCompanyName.equals(profile.insuranceCompanyName) &&
                insurancePolicyNumber.equals(profile.insurancePolicyNumber) &&
                insuranceAgentName.equals(profile.insuranceAgentName) &&
                insuranceAgentPhoneNum.equals(profile.insuranceAgentPhoneNum) &&
                imageUrl.equals(profile.imageUrl);
    }

}
