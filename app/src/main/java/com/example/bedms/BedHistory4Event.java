package com.example.bedms;

import java.time.LocalDateTime;

public class BedHistory4Event {

    private String eventType;
    private String dateAndTime;


    public BedHistory4Event() {

    }

    public BedHistory4Event(String eventType, String dateAndTime) {
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
