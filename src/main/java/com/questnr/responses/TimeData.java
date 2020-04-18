package com.questnr.responses;

public class TimeData {
    private Integer weeks;
    private Integer days;
    private Integer hours;
    private Integer minutes;
    private Integer seconds;

    private String wString = "w";
    private String dString = "d";
    private String hString = "h";
    private String mString = "m";
    private String sString = "s";


    public TimeData(Integer weeks, Integer days, Integer hours, Integer minutes, Integer seconds) {
        this.weeks = weeks;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Integer getWeeks() {
        return weeks;
    }

    public void setWeeks(Integer weeks) {
        this.weeks = weeks;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public String getTimePartString(Integer part, String partString) {
        if (part <= 0) {
            return "";
        }
        if (part > 1) {
            partString += "s";
        }
        return part + " " + partString;
    }

    public String getTimeString() {
        String hString = this.getTimePartString(hours, "hour");
        String mString = this.getTimePartString(minutes, "minute");
        String sString = this.getTimePartString(seconds, "second");
        return (hString + " " + mString + " " + sString).trim();
    }

    public String getMaxTimePart() {
        if (weeks > 0) {
            return weeks + this.wString;
        } else if (days > 0) {
            return days + this.dString;
        } else if (hours > 0) {
            return hours + this.hString;
        } else if (minutes > 0) {
            return minutes + this.mString;
        } else {
            return seconds + this.sString;
        }
    }
}
