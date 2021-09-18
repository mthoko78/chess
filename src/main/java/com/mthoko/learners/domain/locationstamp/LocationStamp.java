package com.mthoko.learners.domain.locationstamp;

import com.mthoko.learners.common.entity.UniqueEntity;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class LocationStamp extends UniqueEntity {

    private String imei;

    private String deviceRequested;

    private String latitude;

    private String longitude;

    private Date timeCaptured;

    public LocationStamp() {
    }

    public LocationStamp(String imei, String deviceRequested, String latitude, String longitude, Date timeCaptured) {
        this.imei = imei;
        this.deviceRequested = deviceRequested;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeCaptured = timeCaptured;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDeviceRequested() {
        return deviceRequested;
    }

    public void setDeviceRequested(String deviceRequested) {
        this.deviceRequested = deviceRequested;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getTimeCaptured() {
        return timeCaptured;
    }

    public void setTimeCaptured(Date timeCaptured) {
        this.timeCaptured = timeCaptured;
    }

    @Override
    public String getUniqueIdentifier() {
        return imei + "|" + (timeCaptured == null ? null : timeCaptured.getTime());
    }

    @Override
    public String toString() {
        return "LocationStamp [id=" + getId() + ", imei=" + imei + ", deviceRequested=" + deviceRequested
                + ", latitude=" + latitude + ", longitude=" + longitude + ", timeCaptured=" + timeCaptured + "]";
    }

}