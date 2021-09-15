package com.mthoko.mobile.domain.choice;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.question.Question;

import java.util.List;
import java.util.Map;

public interface ChoiceService extends BaseService<Choice> {

	List<Choice> findByQuestionNumberAndCategory(int questionNum, String category);

	List<Choice> findByCategoryId(Long id);

	List<Choice> findByCategoryName(String categoryName);

	Map<Integer, List<Choice>> saveChoices(Category category);

	int countByCategoryName(String categoryName);

    Map<Category, Map<Integer, List<Choice>>> populateChoices(List<Category> categories, List<Question> questions);
}
