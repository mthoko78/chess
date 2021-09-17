package com.mthoko.mobile.domain.question;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.question.answer.Answer;
import com.mthoko.mobile.domain.question.image.QuestionImage;

import java.util.List;

public interface QuestionService extends BaseService<Question> {

    Question findByQuestionNumberAndCategory(int questionNum, String category);

    List<Question> populateQuestionTable(Category category);

    List<Question> findByCategoryId(Long id);

    List<Answer> findAnswersByCategoryId(Long id);

    void setQuestionType(Question question);

    List<Question> findByCategoryName(String category);

    List<Question> findByType(Integer type);

    long countByCategoryId(Long categoryId);

    long countByCategoryName(String categoryName);

    List<Question> populateQuestions(List<Category> categories);

    Question findByCategoryNumberAndQuestionNumber(Integer categoryNumber, Integer questionNumber);

    List<Question> findByCategoryNumber(Integer categoryNumber);
}
