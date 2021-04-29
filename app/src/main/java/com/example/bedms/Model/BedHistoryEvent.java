package com.example.bedms.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

public class BedHistoryEvent {

    private String eventType;
    private String dateAndTime;


    public BedHistoryEvent() {

    }

    public BedHistoryEvent(String eventType, String dateAndTime) {
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
