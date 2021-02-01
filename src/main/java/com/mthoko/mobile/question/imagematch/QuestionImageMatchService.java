package com.mthoko.mobile.question.imagematch;

import java.util.List;
import java.util.Map;

import com.mthoko.mobile.category.Category;
import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.question.Question;

public interface QuestionImageMatchService extends BaseService<QuestionImageMatch> {

	Map<Integer, List<QuestionImageMatch>> extractQuestionImageMatches(Category category, List<Question> questions);

	void allocateMatchesToQuestions(List<Question> questions, Map<Integer, List<QuestionImageMatch>> choicesMap);

	Map<Integer, List<QuestionImageMatch>> saveQuestionImageMatches(Category category, List<Question> questions);

	List<QuestionImageMatch> findByCategoryId(Long id);
}
