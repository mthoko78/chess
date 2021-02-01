package com.mthoko.mobile.question.answer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepo extends JpaRepository<Answer, Long> {

	@Query("select a from Answer a join Question q on a = q.answer where q.id = ?1")
	public Answer expectedSelectionByQuestionId(Long questionId);

	@Query("select a from Answer a join Question q on a = q.answer where q.category.id = ?1")
	public List<Answer> findByCategoryId(Long id);

	@Query("select a from Answer a join Question q on a = q.answer where q.number = ?1 and q.category.name = ?2")
	public Optional<Answer> findByQuestionNumberAndCategory(int questionNum, String category);
}
