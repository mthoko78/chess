package com.mthoko.mobile.domain.question;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.choice.Choice;
import com.mthoko.mobile.domain.choice.span.ChoiceSpan;
import com.mthoko.mobile.domain.question.answer.Answer;
import com.mthoko.mobile.domain.question.image.QuestionImage;

import java.util.List;
import java.util.Map;

public interface QuestionService extends BaseService<Question> {

	QuestionImage findByQuestionId(Long id);

	Question findByQuestionNumberAndCategory(int questionNum, String category);

	List<Question> populateQuestionTable(Category category);

	List<Question> findByCategoryId(Long id);

	List<Answer> findAnswersByCategoryId(Long id);

	void setQuestionType(Question question);

	List<Question> findByCategoryName(String category);

	void allocateChoicesToQuestions(Category category, List<Question> questions, Map<Integer, List<Choice>> choicesMap);

	void allocateChoiceSpansToQuestions(Category category, List<Question> byCat,
			Map<Integer, List<ChoiceSpan>> savedChoices);

	List<Question> findByType(Integer type);

	long countByCategoryId(Long categoryId);

	long countByCategoryName(String categoryName);
}
