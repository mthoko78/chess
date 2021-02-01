package com.mthoko.mobile.choice;

import java.util.List;
import java.util.Map;

import com.mthoko.mobile.category.Category;
import com.mthoko.mobile.common.BaseService;

public interface ChoiceService extends BaseService<Choice> {

	List<Choice> findByQuestionNumberAndCategory(int questionNum, String category);

	List<Choice> findByCategoryId(Long id);

	List<Choice> findByCategoryName(String categoryName);

	Map<Integer, List<Choice>> saveChoices(Category category);

	int countByCategoryName(String categoryName);
}
