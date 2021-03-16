package com.example.bedms;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class StringToTimestampFormat {

    String dateString;
    Date date;

    public StringToTimestampFormat() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void timestamp() throws ParseException {

        // stringDate="31/12/1998";
        dateString = "2021-02-23 17:05:34";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = format.parse(dateString);
        System.out.println("This is date time returned " + ": " + date);
        System.out.println("This is date time string  " + ": " + dateString);

        // ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
        // LocalDateTime selectedLocalDate = zone.toLocalDateTime();
        // you can change format of date
/*
        Date netDate = (new Date());
       // Timestamp timestamp = new Timestamp(netDate.getTime());
        //instead of date put your converted date
      //  Timestamp myTimeStamp = timestamp;
        //Date date = (Date) dateFormatter.parse(stringDate);
        //Timestamp timeStampDate = new Timestamp(date.getTime());
        System.out.println("This is date time returned " + ": "+ netDate);
        LocalDateTime now = LocalDateTime.now();
        System.out.println("This is time now " + ": "+ now);
       // Duration duration = Duration.between(dateTime, now);// essentially "duration (mod 1 day)"
        //Period period = Period.between(dateTime.toLocalDate(), now.toLocalDate());
        /*
        System.out.println("The period in between with years" + period.getYears());
        System.out.println("The period in between with months " + period.getMonths());
        System.out.println("The period in between with days " + period.getDays());
        System.out.println("The duration in between with seconds " + duration.getSeconds());
        System.out.println("The duration in between with minutes " + (duration.getSeconds() / 60) );
        System.out.println("The duration in between with hours " + (duration.getSeconds() / 60 / 60) );

         */


    }
}
