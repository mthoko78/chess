package com.mthoko.mobile.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface EntityOperations<T> {

	String UPDATE_ALL = "update-all";
	String UPDATE = "update";
	String DELETE_BY_IDS = "delete/ids";
	String DELETE_BY_ID = "delete/id/{id}";
	String COUNT_BY_ID = "count/id/{id}";
	String RETRIEVE_ALL = "retrieve-all";
	String FIND_BY_ID = "{id}";
	String SAVE = "save";
	String SAVE_ALL = "save-all";
	String DELETE_ALL = "delete-all";
	String DELETE = "delete";

	@GetMapping({ RETRIEVE_ALL, "", "/" })
	List<T> retrieveAll();

	@GetMapping(COUNT_BY_ID)
	Long countById(@PathVariable("id") Long id);

	@DeleteMapping(DELETE_BY_ID)
	void deleteById(@PathVariable("id") Long id);

	@DeleteMapping(DELETE_BY_IDS)
	void deleteByIdsIn(@RequestBody List<Long> ids);

	@PutMapping(UPDATE)
	Object update(@RequestBody T entity);

	@PutMapping(UPDATE_ALL)
	Object updateAll(@RequestBody List<T> entities);

	@GetMapping(FIND_BY_ID)
	T findById(@PathVariable("id") Long id);

	@PostMapping(SAVE)
	T save(@RequestBody T entity);

	@PostMapping(SAVE_ALL)
	List<T> saveAll(@RequestBody List<T> entity);

	@DeleteMapping(DELETE_ALL)
	Object deleteAll(@RequestBody List<T> entities);

	@DeleteMapping(DELETE)
	Object delete(@RequestBody T entity);

}
