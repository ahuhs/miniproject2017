package com.a4dotsinc.profilo;

/**
 * Created by ARAVIND on 31-01-2018.
 */

public class MapRecycler {

    private String lat;
    private String lon;
    private String name;
    private String imgUrl;

    public MapRecycler(){

    }

    public MapRecycler( String lat, String lon, String name, String imagem) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.imgUrl = imagem;
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
