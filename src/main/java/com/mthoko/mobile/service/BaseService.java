package com.mthoko.mobile.service;

import java.util.List;
import java.util.Map;

import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;

public interface BaseService<T extends UniqueEntity> {

    String getProperty(String key);

    void setProperty(String key, String value);

    void removeVerified(List<T> unverified);

    boolean isConnectionAvailable();

    BaseResourceRemote getRemoteResource();

    String getAppProperty(String propertyName);

    boolean openRemoteConnection();

    void closeRemoteConnection();

    boolean beginRemoteTransaction();

    void closeRemoteTransaction();

    void closeRemoteConnectionIf(boolean openRemoteConnection);

    void endRemoteTransactionIf(boolean inTransaction);
}
