package com.example.bedms;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class StringToLDTFormat {

    public StringToLDTFormat(){

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime LDTDate (String stringDate) throws ParseException {

       // stringDate="31/12/1998";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

       // ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
       // LocalDateTime selectedLocalDate = zone.toLocalDateTime();
        LocalDateTime dateTime = LocalDateTime.parse(stringDate, dateFormatter);
        System.out.println("This is date time returned " + ": "+ dateTime);
        LocalDateTime now = LocalDateTime.now();
        System.out.println("This is time now " + ": "+ now);
        Duration duration = Duration.between(dateTime, now);// essentially "duration (mod 1 day)"
        Period period = Period.between(dateTime.toLocalDate(), now.toLocalDate());
        System.out.println("The period in between with years" + period.getYears());
        System.out.println("The period in between with months " + period.getMonths());
        System.out.println("The period in between with days " + period.getDays());
        System.out.println("The duration in between with seconds " + duration.getSeconds());
        System.out.println("The duration in between with minutes " + (duration.getSeconds() / 60) );
        System.out.println("The duration in between with hours " + (duration.getSeconds() / 60 / 60) );





        return dateTime;

    }
}
