package com.mthoko.learners.common.controller;

import com.mthoko.learners.common.entity.BaseEntity;
import com.mthoko.learners.common.service.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3000", "https://printing-services.herokuapp.com", "https://mthoko.herokuapp.com"})
public abstract class BaseController<T extends BaseEntity> implements EntityOperations<T> {

    public abstract BaseService<T> getService();

    @Override
    public T save(T entity) {
        return getService().save(entity);
    }

    @Override
    public List<T> saveAll(List<T> entities) {
        return getService().saveAll(entities);
    }

    @Override
    public void deleteAll(List<T> entities) {
        getService().deleteAll(entities);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void delete(T entity) {
        getService().delete(entity);
    }

    @Override
    public List<T> retrieveAll() {
        return getService().retrieveAll();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(Long id) {
        getService().deleteById(id);
    }

    @Override
    public void deleteByIdsIn(List<Long> ids) {
        getService().deleteByIdsIn(ids);
    }

    @Override
    public Object update(T entity) {
        return getService().update(entity);
    }

    @Override
    public Object updateAll(List<T> entities) {
        return getService().updateAll(entities);
    }

    @Override
    public Optional<T> findById(Long id) {
        return getService().findById(id);
    }

    @Override
    public long count() {
        return getService().count();
    }
}
