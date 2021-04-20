package com.example.bedms;

import java.io.Serializable;
import java.util.ArrayList;

public class BedInfo implements Serializable {
    String bedId;
    String ward;
    int currentStatus;
    //how to set Open, Allocated, Occupier, Cleaning, NotYetCreated here?
    ArrayList <BedHistoryEvent> bedHistory = new ArrayList<>();
    //need neew????


    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public ArrayList<BedHistoryEvent> getBedHistory() {
        return bedHistory;
    }

    public void setBedHistory(ArrayList<BedHistoryEvent> bedHistory) {
        this.bedHistory = bedHistory;
    }
}
