package com.mthoko.mobile.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.mthoko.mobile.entity.Property;
import com.mthoko.mobile.entity.UniqueEntity;

@Transactional
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

}
