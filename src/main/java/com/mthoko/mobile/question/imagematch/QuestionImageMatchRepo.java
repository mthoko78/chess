package com.mthoko.mobile.question.imagematch;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionImageMatchRepo extends JpaRepository<QuestionImageMatch, Long> {

	@Query("select q.matches from Question q where q.id = :id")
	List<QuestionImageMatch> findByQuestionId(@Param("id") Long id);

	@Query("select q.matches from Question q where q.number = :questionNum and q.category.name = :categoryName")
	List<QuestionImageMatch> findByQuestionNumberAndCategory(@Param("questionNum") int questionNum,
			@Param("categoryName") String categoryName);

	@Query("select q.matches from Question q where q.category.id = :id")
	List<QuestionImageMatch> findByCategoryId(@Param("id") Long id);

	@Query("select q.matches from Question q where q.category.name = :categoryName")
	List<QuestionImageMatch> findByCategoryName(@Param("categoryName") String categoryName);

}