package com.mthoko.learners.common.service;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.domain.property.Property;
import com.mthoko.learners.domain.property.PropertyRepo;
import com.mthoko.learners.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.mthoko.learners.common.repo.BaseResourceRemote.APPLICATION_PROPERTIES;


public abstract class BaseServiceImpl<T extends UniqueEntity> implements BaseService<T> {

    @Autowired
    private PropertyRepo propertyResource;

    public BaseServiceImpl() {
    }

    @Override
    public String getProperty(String key) {
        Optional<Property> optionalProperty = propertyResource.findByPropertyKey(key);
        return optionalProperty.isPresent() ? optionalProperty.get().getPropertyValue() : null;
    }

    @Override
    public Property setProperty(String key, String value) {
        Optional<Property> optionalProperty = propertyResource.findByPropertyKey(key);
        Property property = new Property(key, value);
        if (optionalProperty.isPresent()) {
            property.setPropertyValue(value);
        }
        return propertyResource.save(property);
    }

    @Override
    public String unsetProperty(String key) {
        Optional<Property> optionalProperty = propertyResource.findByPropertyKey(key);
        if (optionalProperty.isPresent()) {
            propertyResource.deleteById(optionalProperty.get().getId());
            return optionalProperty.get().getPropertyValue();
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
    public <V extends UniqueEntity> V setDateBeforeSave(V entity, Date date) {
        if (entity != null && entity.getDateCreated() == null) {
            entity.setDateCreated(date);
        }
        return entity;
    }

    @Override
    public <V extends UniqueEntity> List<V> setDateBeforeSave(List<V> entities, Date date) {
        if (entities != null) {
            for (V entity : entities) {
                setDateBeforeSave(entity, date);
            }
        }
        return entities;
    }

    @Override
    public List<T> saveAll(List<T> entities) {
        if (entities != null) {
            setDateBeforeSave(entities, new Date());
            return getRepo().saveAll(entities);
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, Long> extractVerification(T entity) {
        Map<String, Long> verification = new HashMap<>();
        UniqueEntity.putVerification(entity, verification);
        return verification;
    }

    @Override
    public Map<String, Long> extractVerification(List<T> entities) {
        Map<String, Long> verification = new HashMap<>();
        entities.forEach((entity) -> UniqueEntity.putVerification(entity, verification));
        return verification;
    }

    @Override
    public Optional<T> findById(Long id) {
        return getRepo().findById(id);
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

    @Override
    public <V extends UniqueEntity> V setDateBeforeUpdate(V entity, Date date) {
        return entity;
    }

    @Override
    public <V extends UniqueEntity> List<V> setDateBeforeUpdate(List<V> entities, Date date) {
        for (V entity : entities) {
            setDateBeforeUpdate(entity, date);
        }
        return entities;
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
    public <E extends UniqueEntity> List<E> extractDuplicates(List<E> entities) {
        List<E> distinct = new ArrayList<>();
        List<E> duplicates = new ArrayList<>();

        entities.forEach((c) -> {
            if (!distinct.contains(c)) {
                distinct.add(c);
            } else {
                duplicates.add(c);
            }
        });

        return duplicates;
    }

    @Override
    public long count() {
        return getRepo().count();
    }
}
