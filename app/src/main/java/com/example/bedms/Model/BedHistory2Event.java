package com.example.bedms.Model;

import java.time.LocalDateTime;

public class BedHistory2Event {

    private String eventType;
    private LocalDateTime dateAndTime;


    public BedHistory2Event() {

    }

    public BedHistory2Event(String eventType, LocalDateTime dateAndTime) {
        this.eventType = eventType;
        this.dateAndTime = dateAndTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
