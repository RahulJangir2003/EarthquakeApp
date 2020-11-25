package com.example.earthqueakreport;

public class report {
     private double mag;
     private String place;
     private String date;
    private String url;
    public report(double mag, String place, String date,String url) {
        this.mag = mag;
        this.place = place;
        this.date = date;
        this.url =  url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getMag() {
        return mag;
    }

    public void setMag(float mag) {
        this.mag = mag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
