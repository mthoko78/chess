package com.mthoko.mobile.domain.choice;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import com.mthoko.mobile.domain.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChoiceServiceImpl extends BaseServiceImpl<Choice> implements ChoiceService {

	private final ChoiceRepo choiceRepo;

	private ChoiceRepoImpl choiceRepoImpl = new ChoiceRepoImpl();

	@Autowired
	public ChoiceServiceImpl(ChoiceRepo choiceRepo, ChoiceRepoImpl choiceRepoImpl) {
		this.choiceRepo = choiceRepo;
		this.choiceRepoImpl = choiceRepoImpl;
	}

	@Override
	public JpaRepository<Choice, Long> getRepo() {
		return choiceRepo;
	}

	@Override
	public List<Choice> findByQuestionNumberAndCategory(int questionNum, String category) {
		return choiceRepo.findByQuestionNumberAndCategory(questionNum, category);
	}

	@Override
	public List<Choice> findByCategoryName(String categoryName) {
		return choiceRepo.findByCategoryName(categoryName);
	}

	@Override
	public Map<Integer, List<Choice>> saveChoices(Category category) {
		if (countByCategoryName(category.getName()) > 0) {
			return new HashMap<>();
		}
		Map<Integer, List<Choice>> choicesMap = choiceRepoImpl.extractChoices(category);
		List<Choice> allChoices = choicesMap.values().stream().reduce(new ArrayList<>(), (target, elements) -> {
			target.addAll(elements);
			return target;
		});
		choiceRepo.saveAll(allChoices);
		return choicesMap;
	}

	@Override
	public int countByCategoryName(String categoryName) {
		return choiceRepo.findByCategoryName(categoryName).size();
	}

	@Override
	public List<Choice> findByCategoryId(Long id) {
		return choiceRepo.findByCategoryId(id);
	}
}
