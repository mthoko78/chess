package com.mthoko.mobile.choice.span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.category.Category;
import com.mthoko.mobile.choice.ChoiceRepoImpl;
import com.mthoko.mobile.common.BaseServiceImpl;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.question.Question;

@Service
public class ChoiceSpanServiceImpl extends BaseServiceImpl<ChoiceSpan> implements ChoiceSpanService {

	private final ChoiceSpanRepo choiceSpanRepo;

	private final ChoiceRepoImpl choiceRepoImpl = new ChoiceRepoImpl();

	@Autowired
	public ChoiceSpanServiceImpl(ChoiceSpanRepo choiceSpanRepo) {
		this.choiceSpanRepo = choiceSpanRepo;
	}

	@Override
	public JpaRepository<ChoiceSpan, Long> getRepo() {
		return choiceSpanRepo;
	}

	@Override
	public List<ChoiceSpan> findByQuestionNumberAndCategory(int questionNum, String category) {
		return choiceSpanRepo.findByQuestionNumberAndCategory(questionNum, category);
	}

	@Override
	public Map<Integer, List<ChoiceSpan>> saveChoiceSpans(Category category) {
		List<ChoiceSpan> existingChoiceSpans = findByCategoryId(category.getId());
		if (!existingChoiceSpans.isEmpty()) {
			return new HashMap<>();
		}
		Map<Integer, List<ChoiceSpan>> choicesMap = choiceRepoImpl.extractChoiceSpans(category);

		List<ChoiceSpan> allSpans = choicesMap.values().stream().reduce(new ArrayList<ChoiceSpan>(),
				(target, elements) -> {
					target.addAll(elements);
					return target;
				});
		saveAll(allSpans);
		print("TOTAL SPANS: " + allSpans.size());
		return choicesMap;
	}

	@Override
	public void allocateChoiceSpansToQuestions(List<Question> questions, Map<Integer, List<ChoiceSpan>> choicesMap) {
		for (Entry<Integer, List<ChoiceSpan>> entry : choicesMap.entrySet()) {
			Integer questionNum = entry.getKey();
			List<ChoiceSpan> choiceSpans = entry.getValue();
			Optional<Question> optionalQuestion = questions.stream()
					.filter((question) -> question.getNumber() == questionNum).findFirst();
			if (!optionalQuestion.isPresent()) {
				throw new ApplicationException("No corresponding question to span: " + choiceSpans);
			}
			Question question = optionalQuestion.get();
			question.setChoiceSpans(choiceSpans);
		}
	}

	@Override
	public List<ChoiceSpan> findByCategoryId(Long id) {
		return choiceSpanRepo.findByCategoryId(id);
	}
}
