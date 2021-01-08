package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;

import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.service.BaseService;

@CrossOrigin(origins = {"http://localhost:3000", "https://printing-services.herokuapp.com", "https://mthoko.herokuapp.com"})
public abstract class BaseController<T extends UniqueEntity> implements EntityOperations<T> {

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
	public Object deleteAll(List<T> entities) {
		return getService().deleteAll(entities);
	}

	@Override
	public Object delete(T entity) {
		return getService().delete(entity);
	}

	@Override
	public List<T> retrieveAll() {
		return getService().retrieveAll();
	}

	@Override
	public Long countById(Long id) {
		return getService().countById(id);
	}

	@Override
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
	public T findById(Long id) {
		return getService().findById(id);
	}

}
