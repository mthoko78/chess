package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Category;
import com.mthoko.learners.persistence.entity.Question;
import com.mthoko.learners.persistence.entity.QuestionImageMatch;

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
