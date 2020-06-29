package com.bridgelabz.bookstoreapp.dto;

public class CustomerDetailsDto {

    private String name;
    private String pincode;
    private String locality;
    private String address;
    private String city;
    private String landmark;
    private String addressType;

    public CustomerDetailsDto(String name, String pincode, String locality, String address, String city, String landmark, String addressType) {
        this.name = name;
        this.pincode = pincode;
        this.locality = locality;
        this.address = address;
        this.city = city;
        this.landmark = landmark;
        this.addressType = addressType;
    }

    public CustomerDetailsDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
}
