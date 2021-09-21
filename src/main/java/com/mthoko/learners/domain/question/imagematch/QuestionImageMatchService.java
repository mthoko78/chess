package com.mthoko.learners.domain.question.imagematch;

import com.mthoko.learners.common.service.BaseService;
import com.mthoko.learners.domain.category.Category;
import com.mthoko.learners.domain.question.Question;

import java.util.List;
import java.util.Map;

public interface QuestionImageMatchService extends BaseService<QuestionImageMatch> {

    Map<Category, Map<Integer, List<QuestionImageMatch>>> extractQuestionImageMatches(List<Question> questions);

    Map<Integer, List<QuestionImageMatch>> extractImageMatches(Category category);

    void allocateMatchesToQuestions(List<Question> questions,
                                    Map<Category, Map<Integer, List<QuestionImageMatch>>> choicesMap);

    List<QuestionImageMatch> findByCategoryId(Long id);

    Map<Category, Map<Integer, List<QuestionImageMatch>>> populateQuestionImageMatches(List<Question> questions);
}
