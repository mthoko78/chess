package com.mthoko.mobile.service;

import java.util.List;

import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;

public interface BaseService {

    String getProperty(String key);

    void setProperty(String key, String value);

    <T extends UniqueEntity> void removeVerified(List<T> unverified);

    boolean isConnectionAvailable();

    BaseResourceRemote<?> getResource();

    String getAppProperty(String propertyName);

    boolean openConnection();

    void closeConnection();

    boolean beginTransaction();

    void endTransaction();

    void closeConnectionIf(boolean openRemoteConnection);

    void endTransactionIf(boolean inTransaction);

    <T extends UniqueEntity> Long save(T entity);

    <T extends UniqueEntity> List<Long> saveAll(List<T> entities);
}
