package com.a4dotsinc.profilo;

/**
 * Created by ARAVIND on 31-01-2018.
 */

public class MapRecycler {

    private String latitude;
    private String longitude;
    private String name;

    public MapRecycler(){

    }

    public MapRecycler(String start, String end, String name) {
        this.latitude = start;
        this.longitude = end;
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

}
