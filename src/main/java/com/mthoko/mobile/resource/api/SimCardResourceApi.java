package com.mthoko.mobile.resource.api;

import android.content.Context;

import com.mthoko.mobile.entity.SimCard;

import java.util.HashMap;
import java.util.Map;

public class SimCardResourceApi extends BaseResourceApi<SimCard> {

    public SimCardResourceApi(Context context) {
        super(context, SimCard.class);
    }

    public SimCard findBySimNo(String simNo) {
        Map<String, String> params = new HashMap<>();
        params.put("key", FIND_SIM_CARD_BY_SIM_NO);
        params.put("value", '"' + simNo + '"');
        return retrieveOneByRequest(params);
    }
}