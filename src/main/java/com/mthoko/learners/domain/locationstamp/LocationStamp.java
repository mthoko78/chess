package com.mthoko.learners.domain.locationstamp;

import com.mthoko.learners.common.entity.UniqueEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationStamp extends UniqueEntity {

    private String imei;

    private String deviceRequested;

    private String latitude;

    private String longitude;

    private Date timeCaptured;

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