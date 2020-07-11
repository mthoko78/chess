package com.mthoko.mobile.service;

import android.content.Context;

import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.resource.remote.BaseResourceRemote;

import java.util.List;
import java.util.Map;

public interface BaseService<T extends UniqueEntity> {

    BaseResource getResource();

    Context getContext();

    boolean openConnection();

    void closeConnection();

    void closeConnectionIf(boolean isOpenConnection);

    String getProperty(String key);

    void setProperty(String key, String value);

    void removeVerified(List<T> unverified);

    boolean isConnectionAvailable();

    BaseResourceRemote getRemoteResource();

    String getCurrentSimNo();

    <T extends UniqueEntity> List<Long> getIds(List<T> contacts);

    String getImei();

    String getDeviceName();

    void verify(T entity, Map<String, Long> verification);

    void verifyAll(List<T> entities, Map<String, Long> verification);

    void update(T entity);

    void updateAll(List<T> entities);

    Long save(T entity);

    List<Long> saveAll(List<T> entities);

    boolean inTransaction();

    String getAppProperty(String propertyName);

    T findById(Long id);

    List<T> findByIdsIn(List<Long> ids);

    String getEntityName();

    List<T> retrieveAll();

    void delete(T entity);

    void deleteAll(List<T> entities);

    void showNotification(String message);

    void setContext(Context context);

    boolean beginTransaction();

    void endTransactionIf(boolean inTransaction);

    void endTransaction();

    boolean openRemoteConnection();

    void closeRemoteConnection();

    boolean beginRemoteTransaction();

    void closeRemoteTransaction();

    void closeRemoteConnectionIf(boolean openRemoteConnection);

    void endRemoteTransactionIf(boolean inTransaction);
}
