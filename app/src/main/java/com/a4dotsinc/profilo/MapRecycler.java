package com.a4dotsinc.profilo;

/**
 * Created by ARAVIND on 31-01-2018.
 */

public class MapRecycler {

    private String lat;
    private String lon;
    private String name;
    private float radius;
    private String imgUrl;
    private Boolean active;

    public MapRecycler(){

    }

    public MapRecycler( String lat, String lon, String name, float radius, String image, Boolean active) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.imgUrl = image;
        this.radius = radius;
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getImage() {
        return imgUrl;
    }

    public String getLatitude() {
        return lat;
    }

    public String getLongitude() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public void setLatitude(String latitude) {
        this.lat = latitude;
    }

    public void setLongitude(String longitude) {
        this.lon = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.imgUrl = image;
    }
}
