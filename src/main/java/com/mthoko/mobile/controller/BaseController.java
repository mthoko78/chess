package com.mthoko.mobile.controller;

import java.util.List;

import com.mthoko.mobile.entity.UniqueEntity;
import com.mthoko.mobile.service.BaseService;

public abstract class BaseController<T extends UniqueEntity> {

	public static final String SAVE = "/save";
	public static final String SAVE_ALL = "/save-all";

	public String getGoogleMapsLink(Double latitude, Double longitude) {
		return String.format("https://www.google.co.za/maps/search/%s,%s", latitude, longitude);
	}

	public Long save(T entity) {
		return getService().save(entity);
	}

	public List<Long> saveAll(List<T> entities) {
		return getService().saveAll(entities);
	}

	protected abstract BaseService getService();

}
