package com.mthoko.mobile.category;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);
}
