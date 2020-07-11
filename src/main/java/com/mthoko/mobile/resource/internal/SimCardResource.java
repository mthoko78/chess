package com.mthoko.mobile.resource.internal;

import android.content.Context;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.ArrayList;
import java.util.List;

public class SimCardResource extends BaseResource<SimCard> {

    public SimCardResource(Context context, DatabaseWrapper databaseWrapper) {
        super(context, SimCard.class, databaseWrapper);
    }

    public List<Long> findMemberIdsForSimCards(List<SimCard> simCards) {
        String simNos = null;
        String simPhones = null;
        boolean first = true;

        for (SimCard sim : simCards) {
            if (first) {
                simNos = String.format("('%s'", sim.getSimNo());
                simPhones = String.format("('%s'", sim.getPhone());
                first = false;
                continue;
            }
            simNos += String.format(", '%s'", sim.getSimNo());
            simPhones += String.format(", '%s'", sim.getPhone());
        }
        simNos += ")";
        simPhones += ")";
        String whereClause = String.format(
                "SELECT memberId FROM %s WHERE simNo IN (%s) OR phone IN (%s)",
                SimCard.class.getSimpleName(), simNos, simPhones
        );
        return retrieveLongsFromQuery(whereClause, "memberId");
    }

    public SimCard findBySimNo(String simNo) {
        if (simNo == null) {
            return null;
        }
        return findOneWhere(String.format("%s.simNo = '%s'", getEntityName(), simNo));
    }

    public SimCard findByPhone(String phone) {
        if (phone == null) {
            return null;
        }
        return findOneWhere(String.format("%s.phone = '%'", getEntityName(), phone));
    }

    public List<SimCard> findByMemberId(Long memberId) {
        if (memberId == null) {
            return new ArrayList<>();
        }
        return findWhere(String.format("%s.memberId = %s", getEntityName(), memberId));
    }

    public Long findMemberIdBySimNo(String simNo) {
        SimCard simCard;
        if (simNo != null && (simCard = findBySimNo(simNo)) != null) {
            return simCard.getMemberId();
        }
        return null;
    }
}