package com.mthoko.learners.domain.account;

import com.mthoko.learners.common.entity.BaseEntity;
import com.mthoko.learners.domain.account.credentials.Credentials;
import com.mthoko.learners.domain.account.member.Member;
import com.mthoko.learners.domain.device.Device;
import com.mthoko.learners.domain.simcard.SimCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Account extends BaseEntity {

    @OneToOne
    private Member member;

    @OneToOne
    private Credentials credentials;

    @OneToMany
    private List<SimCard> simCards;

    @OneToMany
    private List<Device> devices;

    private String primaryPhone;

    private String primaryImei;

    private boolean phoneVerified;

    @Override
    public String getUniqueIdentifier() {
        if (member != null) {
            return member.getUniqueIdentifier();
        }
        return getClass().getName() + ":" + getId();
    }

    @Override
    public String toString() {
        return "Account [id=" + getId() + ", member=" + member + ", credentials=" + credentials + ", simCards="
                + simCards + ", devices=" + devices + "]";
    }

}