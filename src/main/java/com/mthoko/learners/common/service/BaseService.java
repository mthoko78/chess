package com.mthoko.learners.common.service;

import com.mthoko.learners.common.entity.BaseEntity;
import com.mthoko.learners.domain.property.Property;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
public interface BaseService<T extends BaseEntity> {

	String getProperty(String key);

	Property setProperty(String key, String value);

	void removeVerified(List<T> unverified);

	String getAppProperty(String propertyName);

	T save(T entity);

	List<T> saveAll(List<T> entities);

	void deleteAll(List<T> entities);

	void delete(T entity);

	List<T> retrieveAll();

	void deleteById(Long id);

	void deleteByIdsIn(List<Long> ids);

	T update(T entity);

	List<T> updateAll(List<T> entities);

	String unsetProperty(String key);

	Optional<T> findById(Long id);

	<V extends BaseEntity> V setDateBeforeUpdate(V entity, Date date);

	<V extends BaseEntity> List<V> setDateBeforeUpdate(List<V> entities, Date date);

	<E extends BaseEntity> List<E> extractDuplicates(List<E> entities);

	long count();
}
