package com.mthoko.mobile.domain.question.imagematch;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.question.Question;

import java.util.List;
import java.util.Map;

public interface QuestionImageMatchService extends BaseService<QuestionImageMatch> {

    Map<Category, Map<Integer, List<QuestionImageMatch>>> extractQuestionImageMatches(List<Question> questions);

    void allocateMatchesToQuestions(List<Question> questions,
                                    Map<Category, Map<Integer, List<QuestionImageMatch>>> choicesMap);

    List<QuestionImageMatch> findByCategoryId(Long id);

    Map<Category, Map<Integer, List<QuestionImageMatch>>> populateQuestionImageMatches(List<Question> questions);
}
