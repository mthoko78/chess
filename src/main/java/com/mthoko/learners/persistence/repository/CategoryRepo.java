package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);

	@Query("select c.name from Category c")
    List<String> retrieveAllNames();

}
