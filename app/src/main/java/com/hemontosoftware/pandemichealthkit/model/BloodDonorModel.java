package com.hemontosoftware.pandemichealthkit.model;

public class BloodDonorModel {
    String donorName, age, address, bloodGroup, phoneNumber;
    private boolean expanded;

    public BloodDonorModel(String donorName, String age, String address, String bloodGroup, String phoneNumber) {
        this.donorName = donorName;
        this.age = age;
        this.address = address;
        this.bloodGroup = bloodGroup;
        this.phoneNumber = phoneNumber;
        this.expanded = false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
