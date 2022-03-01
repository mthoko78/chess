package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Answer;
import com.mthoko.learners.persistence.entity.Category;
import com.mthoko.learners.persistence.entity.Question;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AnswerService extends BaseService<Answer> {

    Optional<Answer> findByQuestionNumberAndCategory(int questionNum, String category);

    List<Answer> findByCategoryId(Long id);

    Map<Integer, Answer> saveAnswers(Category category, List<Question> questions);

    List<Answer> findAnswersByCategoryId(Long id);

    Map<Category, Map<Integer, Answer>> populateAnswers( List<Question> questions);
}
