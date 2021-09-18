package com.mthoko.mobile.domain.category;

import com.mthoko.mobile.common.service.BaseService;

import java.util.List;
import java.util.Optional;

public interface CategoryService extends BaseService<Category> {

    Optional<Category> findByName(String name);

    List<Category> getCategories();

    List<String> retrieveAllNames();

    List<Category> populateCategories();
}

