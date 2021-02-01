package com.mthoko.mobile.choice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChoiceRepo extends JpaRepository<Choice, Long> {

	@Query("select q.choices from Question q where q.id = :id")
	List<Choice> findByQuestionId(@Param("id") Long id);

	@Query("select q.choices from Question q where q.number = :questionNum and q.category.name = :categoryName")
	List<Choice> findByQuestionNumberAndCategory(@Param("questionNum") int questionNum,
			@Param("categoryName") String categoryName);

	@Query("select q.choices from Question q where q.category.id = :id")
	List<Choice> findByCategoryId(@Param("id") Long id);

	@Query("select q.choices from Question q where q.category.name = :categoryName")
	List<Choice> findByCategoryName(@Param("categoryName") String categoryName);

}