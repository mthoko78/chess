package com.mthoko.mobile.domain.question;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepo extends JpaRepository<Question, Long> {

	Optional<Question> findByNumberAndCategoryName(int questionNum, String category);

	List<Question> findByCategoryId(Long id);

	List<Question> findByCategoryName(String category);

	List<Question> findByType(Integer type);

	long countByCategory_Id(Long categoryId);

	long countByCategory_Name(String categoryName);
}
