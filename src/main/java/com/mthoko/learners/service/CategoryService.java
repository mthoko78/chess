package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService extends BaseService<Category> {

    Optional<Category> findByName(String name);

    List<Category> getCategories();

    List<String> retrieveAllNames();

    List<Category> populateCategories();
}

