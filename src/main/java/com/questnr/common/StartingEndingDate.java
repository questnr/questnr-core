package com.questnr.common;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class StartingEndingDate {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    Date startingDate;
    Date endingDate;

    int daysBefore = 1;

    public StartingEndingDate(){

    }

    public StartingEndingDate(String dateFormat){
        this.dateFormat = new SimpleDateFormat(dateFormat);
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public int getDaysBefore() {
        return daysBefore;
    }

    public void setDaysBefore(int daysBefore) {
        this.daysBefore = daysBefore;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void build(){
        try {
            endingDate = new Date();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nowMinusN = now.minusDays(this.daysBefore);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, nowMinusN.getYear());
            cal.set(Calendar.MONTH, nowMinusN.getMonthValue() - 1);
            cal.set(Calendar.DAY_OF_MONTH, nowMinusN.getDayOfMonth());
            startingDate = cal.getTime();
            startingDate = dateFormat.parse(dateFormat.format(startingDate));
            endingDate = dateFormat.parse(dateFormat.format(endingDate));
        } catch (Exception e) {
            startingDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(startingDate);
            c.add(Calendar.DATE, 1);
            endingDate = c.getTime();
        }
    }
}
