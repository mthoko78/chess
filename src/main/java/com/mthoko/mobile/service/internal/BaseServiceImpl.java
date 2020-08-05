package com.mthoko.mobile.service.internal;

import java.util.List;

import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.service.BaseService;

public abstract class BaseServiceImpl<T extends UniqueEntity> implements BaseService<T> {

    public BaseServiceImpl() {
    }

    @Override
    public String getProperty(String key) {
        boolean openConnection = openConnection();
        String property = getResource().getProperty(key);
        closeConnectionIf(openConnection);
        return property;
    }

    @Override
    public void setProperty(String key, String value) {
        boolean openConnection = openConnection();
        getResource().setProperty(key, value);
        closeConnectionIf(openConnection);
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
        return getResource().isConnectionAvailable();
    }

    @Override
    public String getAppProperty(String propertyName) {
        return getResource().getAppProperty(propertyName);
    }

    @Override
    public boolean openConnection() {
        return getResource().openConnection();
    }

    @Override
    public void closeConnection() {
        getResource().closeConnection();
    }

    @Override
    public boolean beginTransaction() {
        return getResource().beginTransaction();
    }

    @Override
    public void endTransaction() {
        getResource().endTransaction();
    }

    @Override
    public void closeConnectionIf(boolean isOpenConnection) {
        getResource().closeConnectionIf(isOpenConnection);
    }

    @Override
    public void endTransactionIf(boolean inTransaction) {
        getResource().endTransactionIf(inTransaction);
    }


    @Override
    public Long save(T entity) {
		boolean openConnection = openConnection();
        boolean transaction = beginTransaction();
        Long id = getResource().save(entity);
        endTransactionIf(transaction);
        closeConnectionIf(openConnection);
        return id;
    }
}
