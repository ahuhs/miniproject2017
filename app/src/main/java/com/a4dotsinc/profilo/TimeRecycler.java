package com.a4dotsinc.profilo;

/**
 * Created by ARAVIND on 12-01-2018.
 */

public class TimeRecycler {

    private String starttime;
    private String endtime;
    private  Boolean active;

    public TimeRecycler(){

    }

    public TimeRecycler(String start, String end, Boolean active) {
        this.starttime = start;
        this.endtime = end;
        this.active = active;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
