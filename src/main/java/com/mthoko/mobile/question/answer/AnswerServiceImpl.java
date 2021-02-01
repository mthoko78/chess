package com.mthoko.mobile.question.answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mthoko.mobile.category.Category;
import com.mthoko.mobile.choice.Choice;
import com.mthoko.mobile.common.BaseServiceImpl;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.question.Question;

@Service
public class AnswerServiceImpl extends BaseServiceImpl<Answer> implements AnswerService {

	@Autowired
	private AnswerRepo answerRepo;

	private AnswerRepoImpl answerRepoImpl = new AnswerRepoImpl();

	@Override
	public JpaRepository<Answer, Long> getRepo() {
		return answerRepo;
	}

	@Override
	public Optional<Answer> findByQuestionNumberAndCategory(int questionNum, String category) {
		return answerRepo.findByQuestionNumberAndCategory(questionNum, category);
	}

	public String getAnswersAsText(Question q) {
		final StringBuilder result = new StringBuilder();
		Stream<Choice> filter = q.getChoices().stream()
				.filter((choice) -> q.getAnswer().getSelection().contains(choice.getLetter()));
		filter.forEach((choice) -> {
			result.append("\n" + choice.getLetter() + ")" + choice.getText());
		});
		return result.toString();
	}

	@Override
	public Map<Integer, Answer> saveAnswers(Category category, List<Question> questions) {
		questions = questions.stream().filter((question) -> category.equals(question.getCategory()))
				.collect(Collectors.toList());
		List<Answer> existingAnswers = findByCategoryId(category.getId());
		if (!existingAnswers.isEmpty()) {
			return new HashMap<>();
		}
		Map<Integer, Answer> answersMap = answerRepoImpl.extractAnswers(category);
		saveAll(new ArrayList<Answer>(answersMap.values()));
		return answersMap;
	}

	@Override
	public void allocateAnswersToQuestions(Category category, List<Question> questions,
			Map<Integer, Answer> answersMap) {
		questions = questions.stream().filter(q -> category.equals(q.getCategory())).collect(Collectors.toList());
		for (Entry<Integer, Answer> entry : answersMap.entrySet()) {
			Integer questionNum = entry.getKey();
			Answer answer = entry.getValue();
			Optional<Question> optionalQuestion = questions.stream()
					.filter((question) -> question.getNumber() == questionNum).findFirst();
			if (!optionalQuestion.isPresent()) {
				throw new ApplicationException("No corresponding question to answer: " + answer);
			}
			optionalQuestion.get().setAnswer(answer);
		}
	}

	@Override
	public List<Answer> findAnswersByCategoryId(Long id) {
		return answerRepo.findByCategoryId(id);
	}

	@Override
	public List<Answer> findByCategoryId(Long id) {
		return answerRepo.findByCategoryId(id);
	}
}
