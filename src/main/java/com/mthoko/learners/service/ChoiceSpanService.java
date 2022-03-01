package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Category;
import com.mthoko.learners.persistence.entity.ChoiceSpan;
import com.mthoko.learners.persistence.entity.Question;

import java.util.List;
import java.util.Map;

public interface ChoiceSpanService extends BaseService<ChoiceSpan> {

    List<ChoiceSpan> findByQuestionNumberAndCategory(int questionNum, String category);

    List<ChoiceSpan> findByCategoryId(Long id);

    Map<Category, Map<Integer, List<ChoiceSpan>>> populateChoiceSpans(List<Question> questions);
}
