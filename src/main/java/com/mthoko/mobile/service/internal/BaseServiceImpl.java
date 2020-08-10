package com.mthoko.mobile.service.internal;

import java.util.List;

import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.service.BaseService;

public abstract class BaseServiceImpl implements BaseService {

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
	public <T extends UniqueEntity> void removeVerified(List<T> unverified) {
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
	public <E extends UniqueEntity> Long save(E entity) {
		boolean connection = openConnection();
		Long id = getResource().save(entity);
		closeConnectionIf(connection);
		return id;
	}

	@Override
	public <E extends UniqueEntity> List<Long> saveAll(List<E> entities) {
		boolean connection = openConnection();
		List<Long> ids = getResource().saveAll(entities);
		closeConnectionIf(connection);
		return ids;
	}
}
