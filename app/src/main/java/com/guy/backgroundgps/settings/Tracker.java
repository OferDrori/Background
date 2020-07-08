package com.guy.backgroundgps.settings;

public class Tracker {

    private String trackerName;
    private String phoneNumber;
    private double	latitude = 0;
    private double longitude = 0;

    public Tracker(String trackerName, String phoneNumber) {
        this.trackerName = trackerName;
        this.phoneNumber = phoneNumber;

    }

    public Tracker() {

    }

    public String getTrackerName() {
        return trackerName;
    }

    public void setTrackerName(String trackerName) {
        this.trackerName = trackerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
