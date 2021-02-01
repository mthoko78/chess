package com.mthoko.mobile.common;

import static com.mthoko.mobile.common.BaseResourceRemote.APPLICATION_PROPERTIES;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.property.Property;
import com.mthoko.mobile.property.PropertyRepo;;

public abstract class BaseServiceImpl<T extends UniqueEntity> implements BaseService<T> {

	@Autowired
	private PropertyRepo propertyResource;

	public BaseServiceImpl() {
	}

	@Override
	public String getProperty(String key) {
		Property property = propertyResource.findByPropertyKey(key);
		return property == null ? null : property.getPropertyValue();
	}

	@Override
	public Property setProperty(String key, String value) {
		Property property = propertyResource.findByPropertyKey(key);
		if (property == null) {
			property = new Property(key, value);
		} else {
			property.setPropertyValue(value);
		}
		return propertyResource.save(property);
	}

	@Override
	public String unsetProperty(String key) {
		Property property = propertyResource.findByPropertyKey(key);
		if (property != null) {
			propertyResource.delete(property);
			return property.getPropertyValue();
		}
		return null;
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
	public String getAppProperty(String propertyName) {
		InputStream resource = getClass().getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES);
		Properties properties = new Properties();
		try {
			properties.load(resource);
			return properties.getProperty(propertyName);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}

	public abstract JpaRepository<T, Long> getRepo();

	@Override
	public T save(T entity) {
		setDateBeforeSave(entity, new Date());
		return getRepo().save(entity);
	}

	@Override
	public T setDateBeforeSave(T entity, Date date) {
		if (entity.isValid() && entity.getLastModified() == null) {
			entity.setLastModified(date);
		}
		if (!entity.isValid() && entity.getDateCreated() == null) {
			entity.setDateCreated(date);
		}
		return entity;
	}

	@Override
	public List<T> setDateBeforeSave(List<T> entities, Date date) {
		for (T entity : entities) {
			setDateBeforeSave(entity, date);
		}
		return entities;
	}

	@Override
	public List<T> saveAll(List<T> entities) {
		Date date = new Date();
		for (T entity : entities) {
			setDateBeforeSave(entity, date);
		}
		return getRepo().saveAll(entities);
	}

	@Override
	public Map<String, Long> extractVerification(UniqueEntity entity) {
		Map<String, Long> verification = new HashMap<>();
		UniqueEntity.putVerification(entity, verification);
		return verification;
	}

	@Override
	public T findById(Long id) {
		Optional<T> optional = getRepo().findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public T delete(T entity) {
		getRepo().delete(entity);
		return entity;
	}

	@Override
	public List<T> deleteAll(List<T> entities) {
		getRepo().deleteAll(entities);
		return entities;
	}

	@Override
	public List<T> retrieveAll() {
		return getRepo().findAll();
	}

	@Override
	public Long countById(Long id) {
		Example<T> example = null;
		return getRepo().count(example);
	}

	@Override
	public void deleteById(Long id) {
		getRepo().deleteById(id);
	}

	@Override
	public void deleteByIdsIn(List<Long> ids) {
		getRepo().deleteAll(getRepo().findAllById(ids));
	}

	@Override
	public T update(T entity) {
		return save(entity);
	}

	@Override
	public List<T> updateAll(List<T> entities) {
		return saveAll(entities);
	}

	public <E extends UniqueEntity> void removeAll(List<E> entities, List<E> toRemove) {
		List<E> entitiesToRemove = new ArrayList<>();
		for (E entity : entities) {
			for (E entity2 : toRemove) {
				if (entity == entity2) {
					entitiesToRemove.add(entity);
				}
			}
		}
		entities.removeAll(entitiesToRemove);
	}

	@Override
	public List<String> getFileContents(String path) {
		System.out.println(
				"\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n" + path + "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
		InputStream resource = getClass().getClassLoader().getResourceAsStream(path);
		byte[] b;
		try {
			b = new byte[resource.available()];
			resource.read(b);
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		String contents = new String(b);
		String[] linesArray = contents.split("\r\n");
		List<String> lines = Arrays.asList(linesArray);
		return lines;
	}

	@Override
	public void print(Object string) {
		System.out.println("_________________________________\n\n");
		System.out.println(string);
		System.out.println("\n\n_________________________________");
	}

	@Override
	public long count() {
		return getRepo().count();
	}
}
