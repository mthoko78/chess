package com.mthoko.mobile.category;

import java.util.List;
import java.util.Optional;

import com.mthoko.mobile.common.BaseService;

public interface CategoryService extends BaseService<Category> {

	List<Category> populateCategoryTable();

	Optional<Category> findByName(String name);

	List<Category> getCategories();
}
