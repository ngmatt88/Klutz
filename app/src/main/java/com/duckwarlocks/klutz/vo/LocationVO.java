package com.duckwarlocks.klutz.vo;

/**
 * Created by ngmat_000 on 6/7/2015.
 */
public class LocationVO {
    private String mName;
    private double mLatitude;
    private double mLongitude;
    private String mCity;
    private long dbId;



    public String getmCity() {return mCity;}

    public void setmCity(String mCity) {this.mCity = mCity;}

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }
    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }
}
