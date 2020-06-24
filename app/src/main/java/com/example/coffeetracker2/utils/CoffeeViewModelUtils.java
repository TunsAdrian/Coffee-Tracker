package com.example.coffeetracker2.utils;

import java.util.Calendar;
import java.util.Date;

public class CoffeeViewModelUtils {

    private Calendar calendar = Calendar.getInstance();

    public Date getTodayBegin(){
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        return calendar.getTime();
    }

    public Date getTodayEnd(){
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);

        return calendar.getTime();
    }

    public Date getWeekBegin(){
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        return calendar.getTime();
    }

    public Date getWeekEnd(){
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()+6);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);

        return calendar.getTime();
    }
}
