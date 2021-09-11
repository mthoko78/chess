package com.mthoko.mobile.domain.question.image;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.question.Question;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface QuestionImageService extends BaseService<QuestionImage> {

	List<QuestionImage> findByQuestionNumberAndCategory(int questionNum, String category);

	List<QuestionImage> findByCategoryId(Long id);

	Map<Integer, QuestionImage> saveQuestionImages(Category category, List<Question> questions);

	void allocateImagesToQuestions(Category category, List<Question> questions,
			Map<Integer, QuestionImage> savedQuestionImages);

	byte[] getImageAsBytes(Long imageId) throws IOException;
}