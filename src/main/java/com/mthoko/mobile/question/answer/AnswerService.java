package com.mthoko.mobile.question.answer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mthoko.mobile.category.Category;
import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.question.Question;

public interface AnswerService extends BaseService<Answer> {

	Optional<Answer> findByQuestionNumberAndCategory(int questionNum, String category);

	List<Answer> findByCategoryId(Long id);

	Map<Integer, Answer> saveAnswers(Category category, List<Question> questions);

	List<Answer> findAnswersByCategoryId(Long id);

	void allocateAnswersToQuestions(Category category, List<Question> questions,
			Map<Integer, Answer> answersMap);
}
