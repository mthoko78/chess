package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Category;
import com.mthoko.learners.persistence.entity.Choice;
import com.mthoko.learners.persistence.entity.Question;

import java.util.List;
import java.util.Map;

public interface ChoiceService extends BaseService<Choice> {

    List<Choice> findByQuestionNumberAndCategory(int questionNum, String category);

    Map<Category, Map<Integer, List<Choice>>> populateChoices(List<Question> questions);

    List<Choice> findByCategoryId(Long id);

    List<Choice> findByCategoryName(String categoryName);

    int countByCategoryName(String categoryName);

}
