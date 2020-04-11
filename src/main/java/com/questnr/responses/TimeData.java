package com.questnr.responses;

public class TimeData {
    Integer hours;
    Integer minutes;
    Integer seconds;

    public TimeData(Integer hours, Integer minutes, Integer seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
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
        if (hours > 0) {
            return hours + "h";
        } else if (minutes > 0) {
            return minutes + "m";
        } else {
            return seconds + "s";
        }
    }
}
