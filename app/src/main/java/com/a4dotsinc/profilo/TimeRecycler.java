package com.a4dotsinc.profilo;

/**
 * Created by ARAVIND on 12-01-2018.
 */

public class TimeRecycler {

    private String starttime;
    private String endtime;
    private  Boolean active;
    private long st_milli, sto_milli;

    public TimeRecycler(){

    }

    public TimeRecycler(String start, String end, long st_milli, long sto_milli, Boolean active) {
        this.starttime = start;
        this.endtime = end;
        this.active = active;
        this.st_milli = st_milli;
        this.sto_milli = sto_milli;
    }

    public long getSt_milli() {
        return st_milli;
    }

    public void setSt_milli(long st_milli) {
        this.st_milli = st_milli;
    }

    public long getSto_milli() {
        return sto_milli;
    }

    public void setSto_milli(long sto_milli) {
        this.sto_milli = sto_milli;
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
