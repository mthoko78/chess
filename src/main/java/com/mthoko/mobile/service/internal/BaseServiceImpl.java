package com.mthoko.mobile.service.internal;

import android.content.Context;
import android.widget.Toast;

import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.resource.internal.BaseResource;
import com.mthoko.mobile.service.BaseService;

import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T extends UniqueEntity> implements BaseService<T> {

    public BaseServiceImpl() {
    }

    public abstract BaseResource getResource();

    @Override
    public Context getContext() {
        return getResource().getContext();
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
    public void closeConnectionIf(boolean isOpenConnection) {
        getResource().closeConnectionIf(isOpenConnection);
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
        return getRemoteResource().isConnectionAvailable();
    }

    @Override
    public String getCurrentSimNo() {
        return getResource().getCurrentSimNo(getContext());
    }

    @Override
    public <T extends UniqueEntity> List<Long> getIds(List<T> contacts) {
        return this.getResource().getIds(contacts);
    }

    @Override
    public String getImei() {
        return getResource().getImei();
    }

    @Override
    public String getDeviceName() {
        return getResource().getDeviceName();
    }

    @Override
    public void verify(T entity, Map<String, Long> verification) {
        getResource().verify(entity, verification);
    }

    @Override
    public void verifyAll(List<T> entities, Map<String, Long> verification) {
        boolean connection = openConnection();
        boolean transaction = beginTransaction();
        getResource().verifyAll(entities, verification);
        endTransactionIf(transaction);
        closeConnectionIf(connection);
    }

    @Override
    public void update(T entity) {
        boolean connection = openConnection();
        boolean transaction = beginTransaction();
        getResource().update(entity);
        endTransactionIf(transaction);
        closeConnectionIf(connection);
    }

    @Override
    public void updateAll(List<T> entities) {
        boolean connection = openConnection();
        boolean transaction = beginTransaction();
        getResource().updateAll(entities);
        endTransactionIf(transaction);
        closeConnectionIf(connection);
    }

    @Override
    public Long save(T entity) {
        boolean connection = openConnection();
        boolean transaction = beginTransaction();
        Long id = getResource().save(entity);
        endTransactionIf(transaction);
        closeConnectionIf(connection);
        return id;
    }

    @Override
    public List<Long> saveAll(List<T> entities) {
        boolean connection = openConnection();
        boolean transaction = beginTransaction();
        List ids = getResource().saveAll(entities);
        endTransactionIf(transaction);
        closeConnectionIf(connection);
        return ids;
    }

    @Override
    public boolean inTransaction() {
        return getResource().inTransaction();
    }

    @Override
    public String getAppProperty(String propertyName) {
        return getResource().getAppProperty(propertyName);
    }

    @Override
    public T findById(Long id) {
        boolean connection = openConnection();
        T entity = (T) getResource().findById(id);
        closeConnectionIf(connection);
        return entity;
    }

    @Override
    public List<T> findByIdsIn(List<Long> ids) {
        String idsValues = '(' + ids.toString().substring(1).replace(']', ')');
        boolean connection = openConnection();
        List entity = getResource().findWhere(getEntityName() + ".id IN " + idsValues);
        closeConnectionIf(connection);
        return entity;
    }

    @Override
    public String getEntityName() {
        return getResource().getEntityName();
    }

    @Override
    public List<T> retrieveAll() {
        boolean connection = openConnection();
        List entities = getResource().retrieveAll();
        closeConnectionIf(connection);
        return entities;
    }

    @Override
    public void delete(T entity) {
        boolean connection = openConnection();
        boolean transaction = beginTransaction();
        getResource().delete(entity);
        endTransactionIf(transaction);
        closeConnectionIf(connection);
    }

    @Override
    public void deleteAll(List<T> entities) {
        boolean connection = openConnection();
        boolean transaction = beginTransaction();
        getResource().deleteAll(entities);
        endTransactionIf(transaction);
        closeConnectionIf(connection);
    }

    @Override
    public void showNotification(String message) {
        try {
            Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
            toast.show();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean beginTransaction() {
        return getResource().beginTransaction();
    }

    @Override
    public void endTransactionIf(boolean inTransaction) {
        getResource().endTransactionIf(inTransaction);
    }

    @Override
    public void endTransaction() {
        getResource().endTransaction();
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
