package com.example.babycare;

public class Doctor {
    private String doctorName;
    private String specialization;
    private String hospital;
    private String registrationNumber;

    // Constructor
    public Doctor(String doctorName, String specialization, String hospital, String registrationNumber) {
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.hospital = hospital;
        this.registrationNumber = registrationNumber;
    }

    // Getters
    public String getDoctorName() {
        return doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getHospital() {
        return hospital;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    // Setters
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
