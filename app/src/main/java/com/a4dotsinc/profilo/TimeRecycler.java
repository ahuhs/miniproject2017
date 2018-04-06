package com.a4dotsinc.profilo;

/**
 * Created by ARAVIND on 12-01-2018.
 */

public class TimeRecycler {

    private String starttime;
    private String endtime;
    private  Boolean active;
    private String key;
    private int st_hr, st_min, sto_hr, sto_min;

    public TimeRecycler(){

    }


    public TimeRecycler(String starttime, String endtime, int st_hr, int st_min, int sto_hr, int sto_min, Boolean active, String key) {
        this.starttime = starttime;
        this.endtime = endtime;
        this.active = active;
        this.st_hr = st_hr;
        this.st_min = st_min;
        this.sto_hr = sto_hr;
        this.sto_min = sto_min;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSt_hr() {
        return st_hr;
    }

    public void setSt_hr(int st_hr) {
        this.st_hr = st_hr;
    }

    public int getSt_min() {
        return st_min;
    }

    public void setSt_min(int st_min) {
        this.st_min = st_min;
    }

    public int getSto_hr() {
        return sto_hr;
    }

    public void setSto_hr(int sto_hr) {
        this.sto_hr = sto_hr;
    }

    public int getSto_min() {
        return sto_min;
    }

    public void setSto_min(int sto_min) {
        this.sto_min = sto_min;
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
