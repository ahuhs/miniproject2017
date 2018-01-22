package com.a4dotsinc.profilo;

/**
 * Created by ARAVIND on 12-01-2018.
 */

public class TimeRecycler {

    private String starttime;
    private String endtime;

    public TimeRecycler(){

    }

    public TimeRecycler(String start, String end) {
        this.starttime = start;
        this.endtime = end;
    }

    public String getHead() {
        return starttime;
    }

    public String getDesc() {
        return endtime;
    }

}
