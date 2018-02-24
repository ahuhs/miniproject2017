package com.a4dotsinc.profilo;

/**
 * Created by ARAVIND on 24-02-2018.
 */

public class FlagedLoc {

    String lat;
    String lon;
    String flag;

    public FlagedLoc(String lat, String lon, String flag) {
        this.lat = lat;
        this.lon = lon;
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
