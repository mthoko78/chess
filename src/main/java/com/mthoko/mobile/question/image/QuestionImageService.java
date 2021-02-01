package com.mthoko.mobile.question.image;

import java.util.List;
import java.util.Map;

import com.mthoko.mobile.category.Category;
import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.question.Question;

public interface QuestionImageService extends BaseService<QuestionImage> {

	List<QuestionImage> findByQuestionNumberAndCategory(int questionNum, String category);

	List<QuestionImage> findByCategoryId(Long id);

	Map<Integer, QuestionImage> saveQuestionImages(Category category, List<Question> questions);

	void allocateImagesToQuestions(Category category, List<Question> questions,
			Map<Integer, QuestionImage> savedQuestionImages);

}