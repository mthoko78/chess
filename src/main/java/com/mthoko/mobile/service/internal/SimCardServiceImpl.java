package com.mthoko.mobile.service.internal;

import android.content.Context;

import com.mthoko.mobile.entity.SimCard;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.internal.SimCardResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;
import com.mthoko.mobile.resource.remote.SimCardResourceRemote;
import com.mthoko.mobile.service.SimCardService;
import com.mthoko.mobile.util.ConnectionWrapper;
import com.mthoko.mobile.util.DatabaseWrapper;

import java.util.List;

public class SimCardServiceImpl extends BaseServiceImpl<SimCard> implements SimCardService {

    private final SimCardResource simCardResource;
    private final SimCardResourceRemote simCardResourceRemote;

    public SimCardServiceImpl(Context context) {
        simCardResource = new SimCardResource(context, new DatabaseWrapper());
        simCardResourceRemote = new SimCardResourceRemote(context, new ConnectionWrapper(null));
    }

    public SimCard findByPhone(String phone) {
        SimCard simCard = simCardResource.findByPhone(phone);
        return simCard;
    }

    public SimCard findBySimNo(String simNo) {
        SimCard simCard = simCardResource.findBySimNo(simNo);
        return simCard;
    }

    @Override
    public void setContext(Context context) {
        simCardResource.setContext(context);
    }

    public void verify(SimCard simCard, Long verificationId) {
        simCard.setVerificationId(verificationId);
        update(simCard);
    }

    public List<SimCard> findByMemberId(Long memberId) {

        List<SimCard> simCards = simCardResource.findByMemberId(memberId);

        return simCards;
    }

    public void updateSimCardUsingAnother(SimCard simCard, SimCard thatSimCard) {
        simCard.setPhone(thatSimCard.getPhone());
        simCard.setSimNo(thatSimCard.getSimNo());
        simCard.setVerificationId(thatSimCard.getVerificationId());
        update(simCard);
    }

    public List<Long> findMemberIdsForSimCards(List<SimCard> values) {

        List<Long> ids = simCardResource.findMemberIdsForSimCards(values);

        return ids;
    }

    @Override
    public BaseResource getResource() {
        return simCardResource;
    }

    @Override
    public Context getContext() {
        return simCardResource.getContext();
    }

    @Override
    public BaseResourceRemote getRemoteResource() {
        return simCardResourceRemote;
    }
}
