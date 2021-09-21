package com.mthoko.learners.domain.question.image;

import com.mthoko.learners.common.service.BaseService;
import com.mthoko.learners.domain.category.Category;
import com.mthoko.learners.domain.question.Question;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface QuestionImageService extends BaseService<QuestionImage> {

    List<QuestionImage> findByQuestionNumberAndCategory(int questionNum, String category);

    Map<Integer, QuestionImage> extractQuestionImages(Category category, List<Question> questions);

    List<QuestionImage> findByCategoryId(Long id);

    Map<Integer, QuestionImage> saveQuestionImages(Category category, List<Question> questions);

    byte[] getImageAsBytes(Long imageId) throws IOException;

    Map<Category, Map<Integer, QuestionImage>> populateQuestionImages(List<Question> questions);

    Map<Category, Map<Integer, QuestionImage>> extractAllImages(List<Question> questions, List<Category> categories);
}