package com.mthoko.mobile.presentation.controller;

import com.mthoko.mobile.util.MyConstants;
import com.mthoko.mobile.service.BaseService;

public abstract class BaseController implements MyConstants {

    public abstract BaseService getService();

    public boolean isConnectionAvailable() {
        return getService().isConnectionAvailable();
    }

    public String getCurrentSimNo() {
        return getService().getCurrentSimNo();
    }
}
