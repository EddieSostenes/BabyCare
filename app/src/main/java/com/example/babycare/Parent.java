package com.example.babycare;

public class Parent {
    private String babyName;
    private String babyAge; // Assuming baby's age is an integer
    private String babyPlaceOfBirth;
    private String parentName;
    private String placeOfResidence;
    private String phone;

    // Constructor
    public Parent(String babyName, String babyAge, String babyPlaceOfBirth, String parentName, String placeOfResidence, String phone) {
        this.babyName = babyName;
        this.babyAge = babyAge;
        this.babyPlaceOfBirth = babyPlaceOfBirth;
        this.parentName = parentName;
        this.placeOfResidence = placeOfResidence;
        this.phone = phone;
    }

    // Getters
    public String getBabyName() {
        return babyName;
    }

    public String getBabyAge() {
        return babyAge;
    }

    public String getBabyPlaceOfBirth() {
        return babyPlaceOfBirth;
    }

    public String getParentName() {
        return parentName;
    }

    public String getPlaceOfResidence() {
        return placeOfResidence;
    }

    public String getPhone() {
        return phone;
    }

    // Setters
    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public void setBabyAge(String babyAge) {
        this.babyAge = babyAge;
    }

    public void setBabyPlaceOfBirth(String babyPlaceOfBirth) {
        this.babyPlaceOfBirth = babyPlaceOfBirth;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setPlaceOfResidence(String placeOfResidence) {
        this.placeOfResidence = placeOfResidence;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}