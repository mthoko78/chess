package com.mthoko.mobile.common;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mthoko.mobile.property.Property;

@Service
public interface BaseService<T extends UniqueEntity> {

	String getProperty(String key);

	Property setProperty(String key, String value);

	void removeVerified(List<T> unverified);

	String getAppProperty(String propertyName);

	T save(T entity);

	List<T> saveAll(List<T> entities);

	Map<String, Long> extractVerification(UniqueEntity entity);

	List<T> deleteAll(List<T> entities);

	T delete(T entity);

	List<T> retrieveAll();

	Long countById(Long id);

	void deleteById(Long id);

	void deleteByIdsIn(List<Long> ids);

	T update(T entity);

	List<T> updateAll(List<T> entities);

	String unsetProperty(String key);

	T findById(Long id);

	T setDateBeforeSave(T entity, Date date);

	List<T> setDateBeforeSave(List<T> entities, Date date);

	List<String> getFileContents(String path) throws IOException;

	long count();

	void print(Object object);

}
