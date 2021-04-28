package com.example.bedms.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Bed {

    private String bedName;
    private String bedType;
    private String patientID;
    private String bedStatus;
    private String bedWard;
    private String bedId;
    private Hashtable<String,BedHistoryEvent> bedHistoryEventHashTable;

    //List<String> bed =  new ArrayList<>();
    public Bed(){
        this.bedHistoryEventHashTable = new Hashtable<>();
    }

    public Bed(String bedName, String bedType, String patientID, String bedStatus, String bedWard, String bedId ){
        this.bedName = bedName;
        this.bedType = bedType;
        this.patientID = patientID;
        this.bedStatus = bedStatus;
        this.bedWard = bedWard;
        this.bedId = bedId;
        this.bedHistoryEventHashTable = new Hashtable<>();
    }

    public Bed(String bedName, String bedType, String patientID, String bedStatus, String bedWard, String bedId, Hashtable<String,BedHistoryEvent> bedHistoryEventHashTable ){
        this.bedName = bedName;
        this.bedType = bedType;
        this.patientID = patientID;
        this.bedStatus = bedStatus;
        this.bedWard = bedWard;
        this.bedId = bedId;
        this.bedHistoryEventHashTable = bedHistoryEventHashTable;
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

    public void setBedHistoryEventHashTable(Hashtable<String,BedHistoryEvent> bedHistoryEventHashTable) { this.bedHistoryEventHashTable = bedHistoryEventHashTable;}

    public Hashtable<String,BedHistoryEvent> getBedHistoryEventHashTable() { return bedHistoryEventHashTable; }

    public BedHistoryEvent getLatestBedHistoryForDay( String dateSelectedAsString) {

        Hashtable<String,BedHistoryEvent> bedHistoryEventHashtable = this.getBedHistoryEventHashTable();
        if(bedHistoryEventHashtable == null || bedHistoryEventHashtable.size() == 0){
            return null;
        }

        Enumeration<String> keys = bedHistoryEventHashtable.keys();
        BedHistoryEvent LatestEvent = null;

        Date dateSelected = parseDateFromScreen(dateSelectedAsString);
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            BedHistoryEvent Event = bedHistoryEventHashtable.get(key);
            Date EventDate = parseDateFromDB(Event.getDateAndTime());

            //Storing the eventType for transactions before or equal to the selectedDate
            if (EventDate.compareTo(dateSelected) <= 0) {
                if(LatestEvent==null){
                    //first occurrence, no need to compare
                    LatestEvent = Event;
                }else{
                    Date LatestEventDate = parseDateFromDB(LatestEvent.getDateAndTime());
                    if (LatestEventDate.compareTo(EventDate) <=0){
                        LatestEvent = Event;
                    }
                }
            }
        }
        return LatestEvent;
    }

    private Date parseDateFromDB(String dateAsString){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        if(dateAsString == null){
            return date;
        }
        try {
            date = format.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Date parseDateFromScreen(String dateAsString){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = null;
        if(dateAsString == null){
            return date;
        }
        try {
            date = dateFormatter.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
