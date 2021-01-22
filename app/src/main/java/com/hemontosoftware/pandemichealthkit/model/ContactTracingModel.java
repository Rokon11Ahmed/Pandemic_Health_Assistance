package com.hemontosoftware.pandemichealthkit.model;

public class ContactTracingModel {
    String name , age, address, time;

    public ContactTracingModel(String name, String age, String address, String time) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
