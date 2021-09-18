package com.mthoko.learners.domain.choice.span;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChoiceSpanRepo extends JpaRepository<ChoiceSpan, Long> {

	@Query("select q.choiceSpans from Question q where q.id = :id")
	List<ChoiceSpan> findChoiceSpansByQuestionId(@Param("id") Long id);

	@Query("select q.choiceSpans from Question q where q.number = :questionNum and q.category.name = :category")
	List<ChoiceSpan> findByQuestionNumberAndCategory(@Param("questionNum") int questionNum,
			@Param("category") String category);

	@Query("select q.choiceSpans from Question q where q.category.id = :id")
	List<ChoiceSpan> findByCategoryId(@Param("id") Long id);

}