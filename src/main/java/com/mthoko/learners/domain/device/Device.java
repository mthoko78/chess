package com.mthoko.learners.domain.device;

import com.mthoko.learners.common.entity.UniqueEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Device extends UniqueEntity {

    private Long memberId;

    private String imei;

    private String name;

    private Date dateRegistered;

    @Override
    public String getUniqueIdentifier() {
        return imei;
    }

    @Override
    public String toString() {
        return "Device [id=" + getId() + ", memberId=" + memberId + ", imei=" + imei + ", name=" + name + ", dateRegistered="
                + dateRegistered + "]";
    }

}
