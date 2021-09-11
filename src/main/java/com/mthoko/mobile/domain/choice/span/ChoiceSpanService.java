package com.mthoko.mobile.domain.choice.span;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.question.Question;

import java.util.List;
import java.util.Map;

public interface ChoiceSpanService extends BaseService<ChoiceSpan> {

	List<ChoiceSpan> findByQuestionNumberAndCategory(int questionNum, String category);

	List<ChoiceSpan> findByCategoryId(Long id);

	Map<Integer, List<ChoiceSpan>> saveChoiceSpans(Category category);

	void allocateChoiceSpansToQuestions(List<Question> questions, Map<Integer, List<ChoiceSpan>> choicesMap);
}
