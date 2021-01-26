package com.example.bedms.Model;

public class PatientHistoryEvent {

    private String eventType;
    private String dateAndTime;


    public PatientHistoryEvent() {

    }

    public PatientHistoryEvent(String eventType, String dateAndTime) {
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
