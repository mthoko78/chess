package com.mthoko.mobile.choice.span;

import java.util.List;
import java.util.Map;

import com.mthoko.mobile.category.Category;
import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.question.Question;

public interface ChoiceSpanService extends BaseService<ChoiceSpan> {

	List<ChoiceSpan> findByQuestionNumberAndCategory(int questionNum, String category);

	List<ChoiceSpan> findByCategoryId(Long id);

	Map<Integer, List<ChoiceSpan>> saveChoiceSpans(Category category);

	void allocateChoiceSpansToQuestions(List<Question> questions, Map<Integer, List<ChoiceSpan>> choicesMap);
}
