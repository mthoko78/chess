package com.mthoko.mobile.presentation.controller;

import android.content.Context;

import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.service.BaseService;
import com.mthoko.mobile.service.SimContactService;
import com.mthoko.mobile.service.common.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class SimContactsController extends BaseController {


    private final SimContactService service;

    public SimContactsController(Context context) {
        this.service = ServiceFactory.getSimContactService(context);
    }

    @Override
    public BaseService getService() {
        return service;
    }

    public List<SimContact> getSimContactsBySimNo(String simNo) {
        String property = service.getProperty("currentMemberId");
        if (property != null) {
            return service.findBySimNo(simNo);
        }
        return new ArrayList<>();
    }
}
