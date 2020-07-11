package com.mthoko.mobile.service.internal;

import java.util.List;

import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.service.BaseService;

public abstract class BaseServiceImpl<T extends UniqueEntity> implements BaseService<T> {

    public BaseServiceImpl() {
    }

    @Override
    public String getProperty(String key) {
        boolean openConnection = openRemoteConnection();
        String property = getRemoteResource().getProperty(key);
        closeRemoteConnectionIf(openConnection);
        return property;
    }

    @Override
    public void setProperty(String key, String value) {
        boolean openConnection = openRemoteConnection();
        getRemoteResource().setProperty(key, value);
        closeRemoteConnectionIf(openConnection);
    }


    @Override
    public void removeVerified(List<T> unverified) {
        int lastIndex = unverified.size() - 1;
        for (int i = lastIndex; i >= 0; i--) {
            if (unverified.get(i).isVerified()) {
                unverified.remove(i);
            }
        }
    }

    @Override
    public boolean isConnectionAvailable() {
        return getRemoteResource().isConnectionAvailable();
    }

    @Override
    public String getAppProperty(String propertyName) {
        return getRemoteResource().getAppProperty(propertyName);
    }

    @Override
    public boolean openRemoteConnection() {
        return getRemoteResource().openConnection();
    }

    @Override
    public void closeRemoteConnection() {
        getRemoteResource().closeConnection();
    }

    @Override
    public boolean beginRemoteTransaction() {
        return getRemoteResource().beginTransaction();
    }

    @Override
    public void closeRemoteTransaction() {
        getRemoteResource().endTransaction();
    }

    @Override
    public void closeRemoteConnectionIf(boolean openRemoteConnection) {
        getRemoteResource().closeConnectionIf(openRemoteConnection);
    }

    @Override
    public void endRemoteTransactionIf(boolean inTransaction) {
        getRemoteResource().endTransactionIf(inTransaction);
    }
}
