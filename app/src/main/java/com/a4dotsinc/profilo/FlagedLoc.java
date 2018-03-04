package com.a4dotsinc.profilo;

/**
 * Created by ARAVIND on 24-02-2018.
 */

public class FlagedLoc {

    String lat;
    String lon;
    String flag;
    Float radius;
    String key;

    public FlagedLoc(String lat, String lon, String flag, Float radius, String key) {
        this.lat = lat;
        this.lon = lon;
        this.flag = flag;
        this.radius = radius;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
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
