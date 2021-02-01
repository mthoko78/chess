package com.mthoko.mobile.question;

import java.util.List;
import java.util.Map;

import com.mthoko.mobile.category.Category;
import com.mthoko.mobile.choice.Choice;
import com.mthoko.mobile.choice.span.ChoiceSpan;
import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.question.answer.Answer;
import com.mthoko.mobile.question.image.QuestionImage;

public interface QuestionService extends BaseService<Question> {

	QuestionImage findByQuestionId(Long id);

	Question findByQuestionNumberAndCategory(int questionNum, String category);

	Question findById(Long id);

	List<Question> populateQuestionTable(Category category);

	List<Question> findByCategoryId(Long id);

	List<Answer> findAnswersByCategoryId(Long id);

	void setQuestionType(Question question);

	List<Question> findByCategoryName(String category);

	void allocateChoicesToQuestions(Category category, List<Question> questions, Map<Integer, List<Choice>> choicesMap);

	void allocateChoiceSpansToQuestions(Category category, List<Question> byCat,
			Map<Integer, List<ChoiceSpan>> savedChoices);

	List<Question> findByType(Integer type);
}
