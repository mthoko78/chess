package com.mthoko.learners.domain.choice;

import com.mthoko.learners.common.service.BaseService;
import com.mthoko.learners.domain.category.Category;
import com.mthoko.learners.domain.question.Question;

import java.util.List;
import java.util.Map;

public interface ChoiceService extends BaseService<Choice> {

    List<Choice> findByQuestionNumberAndCategory(int questionNum, String category);

    Map<Category, Map<Integer, List<Choice>>> populateChoices(List<Question> questions);

    List<Choice> findByCategoryId(Long id);

    List<Choice> findByCategoryName(String categoryName);

    int countByCategoryName(String categoryName);

}
