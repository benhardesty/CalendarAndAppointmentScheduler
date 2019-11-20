/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyappointmentscheduler.util;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ben Hardesty
 */
public class DateTimeUtil {
    
    /**
     * Convert a Date object and a time string in the format of "HH:MM AMPM" 
     * to a Date object that has the date and time.
     * 
     * @param date
     * @param time
     * @return Date
     */
    public static Date convert_time_to_date(LocalDate date, String time) {
        
        // Get the hour and minute of the start time.
        int hour = Integer.valueOf(time.split(":")[0]);
        int minute = Integer.valueOf(time.split(":")[1].split(" ")[0]);
        String AMPM = time.split(":")[1].split(" ")[1];
        
        // Convert the 12 hour clock to a 24 hour clock.
        if (AMPM.equals("AM")) {
            if (hour == 12) {
                hour = 0;
            }
        } else {
            if (hour != 12) {
                hour += 12;
            }
        }
        
        // Set the calendar with the date and time.
        int year = date.getYear();
        int month = date.getMonthValue() - 1;
        int day = date.getDayOfMonth();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute, 0);
        
        // Return a new Date object.
        return c.getTime();
    }
    
    /**
     * Return a string in the form of "HH:MM AMPM" from a date object.
     * 
     * @param date
     * @return String
     */
    public static String retreive_time_of_day_from_date(Date date) {
        
        // Set a calendar to the time of a received Date object.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        // Get the hour and minute from the calendar.
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String AMPM = "AM";
        
        // Adjust the hour from a 24 hour clock to a 12 hour clock.
        if (hour > 12) {
            hour -= 12;
            AMPM = "PM";
        } else if (hour == 12) {
            AMPM = "PM";
        }
        
        // Add padding to hours and minutes under 10.
        String hourString = "";
        String minuteString = "";
        if (hour < 10) {
            hourString = "0" + String.valueOf(hour);
        } else {
            hourString = String.valueOf(hour);
        }
        if (minute < 10) {
            minuteString = "0" + String.valueOf(minute);
        } else {
            minuteString = String.valueOf(minute);
        }
        
        // Return a string of the time in the form of "HH:MM AMPM".
        return hourString + ":" + minuteString + " " + AMPM;
    }
    
    /**
     * Convert a Date object into a date string of format MM/DD/YYYY.
     * 
     * @param date
     * @return String
     */
    public static String convert_date_object_to_date_string(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        
        String monthString = String.valueOf(month);
        String dayString = String.valueOf(day);
        String yearString = String.valueOf(year);
        
        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }
        if (dayString.length() == 1) {
            dayString = "0" + dayString;
        }
        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }
        
        return monthString + "/" + dayString + "/" + yearString;
    }
    
    /**
     * Returns whether a date is within business hour. Business hours are
     * between 7:00 am and 7:00 pm on any day of the week.
     * 
     * @param date
     * @return boolean
     * @throws java.lang.Exception
     */
    public static boolean withinBusinessHours(Date date) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        if (date.before(calendar.getTime())) {
            throw new Exception("Time is before business hours.");
        }
        
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 1);
        
        if (date.after(calendar.getTime())) {
            throw new Exception("Time is after business hours.");
        }
        
        return true;
    }
    
    /**
    * Return the month name given an integer. January is 1.
    * 
    * @param month
    * @return String
    */
    public static String getMonthString(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
               return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }

        return "";
    }
}
