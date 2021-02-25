package com.example.bedms.Model;

public class Bed {


    private String bedName;
    private String bedType;
    private String patientID;
    private String bedStatus;
    private String bedWard;
    private String bedId;




    //List<String> bed =  new ArrayList<>();
    public Bed(){

    }
    public Bed(String bedName, String bedType, String patientID, String bedStatus, String bedWard, String bedId ){
        this.bedName = bedName;
        this.bedType = bedType;
        this.patientID = patientID;
        this.bedStatus = bedStatus;
        this.bedWard = bedWard;
        this.bedId = bedId;

    }



    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

    public String getBedType() {
        return bedType;
    }


    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getBedStatus() {
        return bedStatus;
    }

    public void setBedStatus(String bedStatus) {
        this.bedStatus = bedStatus;
    }

    public String getBedWard() {
        return bedWard;
    }

    public void setBedWard(String bedWard) {
        this.bedWard = bedWard;
    }


}
