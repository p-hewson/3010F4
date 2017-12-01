package com.example.tiltlogger.myapplication;

/**
 * Created by paul on 2017-11-28.
 */

public class ClassListData
{

    public String itemDate;
    public String itemTime;
    public String itemID;
    public String itemTilt;
    public String itemTemp;

    public ClassListData(String date, String time, String id, String tilt, String temp)
    {
        this.itemDate = date;
        this.itemTime = time;
        this.itemID = id;
        this.itemTilt = tilt;
        this.itemTemp = temp;
    }

    public String getDate() {
        return itemDate;
    }

    public String getTime() {
        return itemTime;
    }

    public String getID() {
        return itemID;
    }

    public String getTilt() {
        return itemTilt;
    }

    public String getTemp() {
        return itemTemp;
    }
}

