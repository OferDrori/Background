package com.guy.backgroundgps;

import android.annotation.SuppressLint;
import android.location.Location;

public class MyLoc {

    private String mProvider;
    private long mTime = 0;
    private long mElapsedRealtimeNanos = 0;
    private double mElapsedRealtimeUncertaintyNanos = 0.0f;
    private double mLatitude = 0.0;
    private double mLongitude = 0.0;
    private double mAltitude = 0.0f;
    private float mSpeed = 0.0f;
    private float mBearing = 0.0f;
    private float mHorizontalAccuracyMeters = 0.0f;
    private float mVerticalAccuracyMeters = 0.0f;
    private float mSpeedAccuracyMetersPerSecond = 0.0f;
    private float mBearingAccuracyDegrees = 0.0f;

    public MyLoc() { }

    @SuppressLint("NewApi")
    public MyLoc(Location location) {
        this.mProvider = location.getProvider();
        this.mTime = location.getTime();
        this.mElapsedRealtimeNanos = location.getElapsedRealtimeNanos();
        this.mLatitude = location.getLatitude();
        this.mLongitude = location.getLongitude();
        this.mAltitude = location.getAltitude();
        this.mSpeed = location.getSpeed();
        this.mBearing = location.getBearing();
        this.mHorizontalAccuracyMeters = location.getAccuracy();


        try {
            this.mBearingAccuracyDegrees = location.getBearingAccuracyDegrees();
            this.mSpeedAccuracyMetersPerSecond = location.getSpeedAccuracyMetersPerSecond();
            this.mElapsedRealtimeUncertaintyNanos = location.getElapsedRealtimeUncertaintyNanos();
            this.mVerticalAccuracyMeters = location.getVerticalAccuracyMeters();
        } catch (NoSuchMethodError ex) { }
    }

    public MyLoc(MyLoc myLoc) {
        this.mProvider = myLoc.mProvider;
        this.mTime = myLoc.mTime;
        this.mElapsedRealtimeNanos = myLoc.mElapsedRealtimeNanos;
        this.mElapsedRealtimeUncertaintyNanos = myLoc.mElapsedRealtimeUncertaintyNanos;
        this.mLatitude = myLoc.mLatitude;
        this.mLongitude = myLoc.mLongitude;
        this.mAltitude = myLoc.mAltitude;
        this.mSpeed = myLoc.mSpeed;
        this.mBearing = myLoc.mBearing;
        this.mHorizontalAccuracyMeters = myLoc.mHorizontalAccuracyMeters;
        this.mVerticalAccuracyMeters = myLoc.mVerticalAccuracyMeters;
        this.mSpeedAccuracyMetersPerSecond = myLoc.mSpeedAccuracyMetersPerSecond;
        this.mBearingAccuracyDegrees = myLoc.mBearingAccuracyDegrees;
    }

    public String getProvider() {
        return mProvider;
    }

    public MyLoc setProvider(String mProvider) {
        this.mProvider = mProvider;
        return this;
    }

    public long getTime() {
        return mTime;
    }

    public MyLoc setTime(long mTime) {
        this.mTime = mTime;
        return this;
    }

    public long getElapsedRealtimeNanos() {
        return mElapsedRealtimeNanos;
    }

    public MyLoc setElapsedRealtimeNanos(long mElapsedRealtimeNanos) {
        this.mElapsedRealtimeNanos = mElapsedRealtimeNanos;
        return this;
    }

    public double getElapsedRealtimeUncertaintyNanos() {
        return mElapsedRealtimeUncertaintyNanos;
    }

    public MyLoc setElapsedRealtimeUncertaintyNanos(double mElapsedRealtimeUncertaintyNanos) {
        this.mElapsedRealtimeUncertaintyNanos = mElapsedRealtimeUncertaintyNanos;
        return this;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public MyLoc setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
        return this;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public MyLoc setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
        return this;
    }

    public double getAltitude() {
        return mAltitude;
    }

    public MyLoc setAltitude(double mAltitude) {
        this.mAltitude = mAltitude;
        return this;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public MyLoc setSpeed(float mSpeed) {
        this.mSpeed = mSpeed;
        return this;
    }

    public float getBearing() {
        return mBearing;
    }

    public MyLoc setBearing(float mBearing) {
        this.mBearing = mBearing;
        return this;
    }

    public float getAccuracy() {
        return mHorizontalAccuracyMeters;
    }

    public MyLoc setAccuracy(float mHorizontalAccuracyMeters) {
        this.mHorizontalAccuracyMeters = mHorizontalAccuracyMeters;
        return this;
    }

    public float getVerticalAccuracyMeters() {
        return mVerticalAccuracyMeters;
    }

    public MyLoc setVerticalAccuracyMeters(float mVerticalAccuracyMeters) {
        this.mVerticalAccuracyMeters = mVerticalAccuracyMeters;
        return this;
    }

    public float getSpeedAccuracyMetersPerSecond() {
        return mSpeedAccuracyMetersPerSecond;
    }

    public MyLoc setSpeedAccuracyMetersPerSecond(float mSpeedAccuracyMetersPerSecond) {
        this.mSpeedAccuracyMetersPerSecond = mSpeedAccuracyMetersPerSecond;
        return this;
    }

    public float getBearingAccuracyDegrees() {
        return mBearingAccuracyDegrees;
    }

    public MyLoc setBearingAccuracyDegrees(float mBearingAccuracyDegrees) {
        this.mBearingAccuracyDegrees = mBearingAccuracyDegrees;
        return this;
    }

//    public LatLng toLatLan() {
//        return new LatLng(this.mLatitude, this.mLongitude);
//    }
}
