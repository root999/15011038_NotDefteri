package com.example.semesterprojectv_0_7;

import android.location.Location;

import java.io.Serializable;
import java.util.Date;

public class Data implements Serializable{

    private String baslik;
    private String text;
    private Location location;
    private String color;
    private String priority;
    private Date date;

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }

    public String getBaslik() {
        return baslik;
    }

    public String getText() {
        return text;
    }

    public Location getLocation() {
        return location;
    }

    public String getColor() {
        return color;
    }
}
