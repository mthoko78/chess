package com.mthoko.mobile.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

public interface EntityOperations<T> {

    String SAVE_ALL = "save-all";
    String UPDATE_ALL = "update-all";
    String DELETE_ALL = "delete-all";
    String DELETE_BY_IDS = "delete/ids";
    String BY_ID = "{id}";
    String COUNT = "count";

    @GetMapping
    List<T> retrieveAll();

    @DeleteMapping(BY_ID)
    ResponseEntity<Object> deleteById(@PathVariable("id") Long id);

    @DeleteMapping(DELETE_BY_IDS)
    void deleteByIdsIn(@RequestBody List<Long> ids);

    @PutMapping
    Object update(@RequestBody T entity);

    @PutMapping(UPDATE_ALL)
    Object updateAll(@RequestBody List<T> entities);

    @GetMapping(BY_ID)
    Optional<T> findById(@PathVariable("id") Long id);

    @PostMapping
    T save(@RequestBody T entity);

    @PostMapping(SAVE_ALL)
    List<T> saveAll(@RequestBody List<T> entity);

    @DeleteMapping(DELETE_ALL)
    Object deleteAll(@RequestBody List<T> entities);

    @DeleteMapping
    Object delete(@RequestBody T entity);

    @GetMapping(COUNT)
    long count();

}
