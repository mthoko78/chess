package com.mthoko.mobile.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepo extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);

	@Query("select c.name from Category c")
    List<String> retrieveAllNames();
}
