package com.example.bedms;

import com.example.bedms.Model.BedHistoryEvent;

public class BedHistoryEventHelper {
    public static int getBedStatusFromEvent(BedHistoryEvent bhe) {

        if (bhe == null){
            return 4;
        }

        int statusCode;

        switch (bhe.getEventType()) {
            case "Added bed":
            case "Bed allocated to ward":
            case "Bed is now open":
            case "Bed is cleaned – ready for next patient":
                statusCode = 0;
                break;
            case "Bed allocated - patient on way":
                statusCode = 2;
                break;
            case "Patient in bed in ward":
                statusCode = 1;
                break;
            case "Bed ready for cleaning":
                statusCode = 3;
                break;
            default:
                statusCode = 4;
                break;
        }
        return statusCode;
    }
}

