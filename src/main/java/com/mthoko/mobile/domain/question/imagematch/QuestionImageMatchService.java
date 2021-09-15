package com.mthoko.mobile.domain.question.imagematch;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.question.Question;

import java.util.List;
import java.util.Map;

public interface QuestionImageMatchService extends BaseService<QuestionImageMatch> {

	Map<Integer, List<QuestionImageMatch>> extractQuestionImageMatches(Category category, List<Question> questions);

	void allocateMatchesToQuestions(List<Question> questions, Map<Integer, List<QuestionImageMatch>> choicesMap);

	Map<Integer, List<QuestionImageMatch>> saveQuestionImageMatches(Category category, List<Question> questions);

	List<QuestionImageMatch> findByCategoryId(Long id);

    Map<Integer, List<QuestionImageMatch>> populateQuestionImageMatches(List<Category> categories, List<Question> questions);
}
