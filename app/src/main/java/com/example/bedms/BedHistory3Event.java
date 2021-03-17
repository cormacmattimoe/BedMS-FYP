package com.example.bedms;

import com.google.firebase.Timestamp;

import java.util.Date;

public class BedHistory3Event {

    private String eventType;
    private String dateAndTime;


    public BedHistory3Event() {

    }

    public BedHistory3Event(String eventType, String dateAndTime) {
        this.eventType = eventType;
        this.dateAndTime = dateAndTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
