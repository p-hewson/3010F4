package com.example.tiltlogger.myapplication;

/**
 * Created by paul on 2017-11-30.
 */

public class ClassListInfo
{

    public String infoID;
    public String infoName;
    public String infoLocation;
    public String infoInterval;

    public ClassListInfo(String id, String name, String location, String interval)
    {
        this.infoID = id;
        this.infoName = name;
        this.infoLocation = location;
        this.infoInterval = interval;
    }

    public String getInfoID() {
        return infoID;
    }

    public String getSensorName() {
        return infoName;
    }

    public String getLocation() {
        return infoLocation;
    }

    public String getInterval() {
        return infoInterval;
    }
}

