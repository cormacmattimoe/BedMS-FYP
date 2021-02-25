package com.example.bedms.Model;

public class Patient {
    private String pName;
    private String pDOB;
    private String pAddress;
    private String pPhoneNumber;
    private String pNextofkin;
    private String pIllness;
    private String bedName;
    private String wardName;
    private String doctor;
    public String status;

    public Patient() {
    }

    public Patient(String pName, String pDOB, String pAddress, String pPhoneNumber, String pNextofkin, String pIllness, String bedName,String wardName, String doctor, String status) {
        this.pName = pName;
        this.pDOB = pDOB;
        this.pAddress = pAddress;
        this.pPhoneNumber = pPhoneNumber;
        this.pNextofkin = pNextofkin;
        this.pIllness = pIllness;
        this.bedName = bedName;
        this.wardName = wardName;
        this.doctor = doctor;
        this.status = status;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpDOB() {
        return pDOB;
    }

    public void setpDOB(String pDOB) {
        this.pDOB = pDOB;
    }

    public String getpAddress() {
        return pAddress;
    }

    public void setpAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public String getpPhoneNumber() {
        return pPhoneNumber;
    }

    public void setpPhoneNumber(String pPhoneNumber) {
        this.pPhoneNumber = pPhoneNumber;
    }

    public String getpNextofkin() {
        return pNextofkin;
    }

    public void setpNextofkin(String pNextofkin) {
        this.pNextofkin = pNextofkin;
    }

    public String getpIllness() {
        return pIllness;
    }

    public void setpIllness(String pIllness) {
        this.pIllness = pIllness;
    }

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


