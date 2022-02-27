package com.mthoko.learners.domain.locationstamp;

import com.mthoko.learners.common.entity.BaseEntity;
import com.mthoko.learners.common.util.DataManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationStamp extends BaseEntity {

    private String imei;

    private String deviceRequested;

    private String latitude;

    private String longitude;

    @Transient
    private String geoLocUrl;

    private Date timeCaptured;

    public String getGeoLocUrl() {
        return DataManager.getGeoLocUrl(latitude, longitude);
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