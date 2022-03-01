package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.QuestionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionImageRepo extends JpaRepository<QuestionImage, Long> {

	@Query("select img from QuestionImage img join Question q on q.image.id = img.id where q.id = ?1")
	List<QuestionImage> findByQuestionId(Long id);

	@Query("select img from QuestionImage img join Question q on q.image.id = img.id where q.number = ?1 and q.category.name = ?2")
	List<QuestionImage> findByQuestionNumberAndCategory(int questionNum, String category);

	@Query("select img from QuestionImage img join Question q on q.image.id = img.id where q.category.id = ?1")
	List<QuestionImage> findByCategoryId(Long id);

}