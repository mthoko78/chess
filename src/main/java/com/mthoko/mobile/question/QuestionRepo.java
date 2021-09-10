package com.mthoko.mobile.question;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question, Long> {

	Optional<Question> findByNumberAndCategoryName(int questionNum, String category);

	List<Question> findByCategoryId(Long id);

	List<Question> findByCategoryName(String category);

	List<Question> findByType(Integer type);

	long countByCategory_Id(Long categoryId);

	long countByCategory_Name(String categoryName);
}
