package com.mthoko.mobile.service;

import java.util.List;
import java.util.Map;

import com.mthoko.mobile.entity.LocationStamp;
import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;

public interface BaseService<T extends UniqueEntity> {

    String getProperty(String key);

    void setProperty(String key, String value);

    void removeVerified(List<T> unverified);

    boolean isConnectionAvailable();

    BaseResourceRemote<T> getResource();

    String getAppProperty(String propertyName);

    boolean openConnection();

    void closeConnection();

    boolean beginTransaction();

    void endTransaction();

    void closeConnectionIf(boolean openRemoteConnection);

    void endTransactionIf(boolean inTransaction);

	Long save(T entity);
}
