package com.mthoko.mobile.common.service;

import com.mthoko.mobile.common.entity.UniqueEntity;
import com.mthoko.mobile.domain.property.Property;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
public interface BaseService<T extends UniqueEntity> {

	String getProperty(String key);

	Property setProperty(String key, String value);

	void removeVerified(List<T> unverified);

	String getAppProperty(String propertyName);

	T save(T entity);

	List<T> saveAll(List<T> entities);

	List<T> deleteAll(List<T> entities);

	T delete(T entity);

	List<T> retrieveAll();

	void deleteById(Long id);

	void deleteByIdsIn(List<Long> ids);

	T update(T entity);

	List<T> updateAll(List<T> entities);

	String unsetProperty(String key);

	Optional<T> findById(Long id);

	<V extends UniqueEntity> V setDateBeforeSave(V entity, Date date);

	<V extends UniqueEntity> List<V> setDateBeforeSave(List<V> entities, Date date);

    Map<String, Long> extractVerification(T entity);

    Map<String, Long> extractVerification(List<T> entities);

	<V extends UniqueEntity> V setDateBeforeUpdate(V entity, Date date);

	<V extends UniqueEntity> List<V> setDateBeforeUpdate(List<V> entities, Date date);

	<E extends UniqueEntity> List<E> extractDuplicates(List<E> entities);

	long count();
}
