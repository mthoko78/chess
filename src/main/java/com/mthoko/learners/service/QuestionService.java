package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Category;
import com.mthoko.learners.persistence.entity.Answer;
import com.mthoko.learners.persistence.entity.Question;

import java.util.List;

public interface QuestionService extends BaseService<Question> {

    Question findByQuestionNumberAndCategory(int questionNum, String category);

    List<Question> findByCategoryId(Long id);

    List<Answer> findAnswersByCategoryId(Long id);

    void setQuestionType(Question question);

    List<Question> findByCategoryName(String category);

    List<Question> findByType(Integer type);

    long countByCategoryId(Long categoryId);

    long countByCategoryName(String categoryName);

    List<Question> extractAllQuestions(List<Category> categories);

    Question findByCategoryNumberAndQuestionNumber(Integer categoryNumber, Integer questionNumber);

    List<Question> findByCategoryNumber(Integer categoryNumber);

    List<Question> rewriteQuestionsToFile(Category category);
}
