package com.mthoko.mobile.question.imagematch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mthoko.mobile.category.Category;
import com.mthoko.mobile.category.CategoryServiceImpl;
import com.mthoko.mobile.common.BaseServiceImpl;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.question.Question;
import com.mthoko.mobile.question.image.QuestionImage;
import com.mthoko.mobile.question.image.QuestionImageService;

@Service
public class QuestionImageMatchServiceImpl extends BaseServiceImpl<QuestionImageMatch>
		implements QuestionImageMatchService {

	private final QuestionImageMatchRepo questionImageMatchRepo;

	private QuestionImageMatchRepoImpl imageMatchRepoImpl = new QuestionImageMatchRepoImpl();

	private QuestionImageService questionImageService;

	@Autowired
	public QuestionImageMatchServiceImpl(QuestionImageMatchRepo imageMatchRepo, QuestionImageService imageRepo) {
		this.questionImageMatchRepo = imageMatchRepo;
		this.questionImageService = imageRepo;
	}

	@Override
	public JpaRepository<QuestionImageMatch, Long> getRepo() {
		return questionImageMatchRepo;
	}

	@Override
	public Map<Integer, List<QuestionImageMatch>> extractQuestionImageMatches(Category category,
			List<Question> questions) {
		questions = questions.stream().filter((q) -> category.equals(q.getCategory())).collect(Collectors.toList());
		Map<Integer, List<QuestionImageMatch>> images = new HashMap<>();
		switch (category.getName()) {
		case CategoryServiceImpl.ROAD_SIGNS_MARKINGS:
			Map<Integer, List<QuestionImageMatch>> choicesMap = imageMatchRepoImpl.getQuestionMatches();
			images.putAll(choicesMap);
			allocateMatchesToQuestions(questions, choicesMap);
		}
		return images;
	}

	@Override
	public void allocateMatchesToQuestions(List<Question> questions,
			Map<Integer, List<QuestionImageMatch>> choicesMap) {
		for (Entry<Integer, List<QuestionImageMatch>> entry : choicesMap.entrySet()) {
			Integer questionNum = entry.getKey();
			List<QuestionImageMatch> matches = entry.getValue();
			Optional<Question> optional = questions.stream().filter((q) -> q.getNumber() == questionNum).findAny();
			if (!optional.isPresent()) {
				throw new ApplicationException("No corresponding question to match");
			}
			optional.get().setMatches(matches);
		}
	}

	@Override
	@Transactional
	public Map<Integer, List<QuestionImageMatch>> saveQuestionImageMatches(Category category,
			List<Question> questions) {
		if (findByCategoryId(category.getId()).size() > 0) {
			return new HashMap<>();
		}
		Map<Integer, List<QuestionImageMatch>> matchesMap = extractQuestionImageMatches(category, questions);
		List<QuestionImageMatch> matches = extractQuestionImageMatches(matchesMap);
		List<QuestionImage> questionImages = extractQuestionImages(matchesMap);

		questionImageService.saveAll(questionImages);
		saveAll(matches);

		print(matches);
		print(questionImages);

		return matchesMap;
	}

	@Override
	public List<QuestionImageMatch> findByCategoryId(Long id) {
		return questionImageMatchRepo.findByCategoryId(id);
	}

	private List<QuestionImageMatch> extractQuestionImageMatches(Map<Integer, List<QuestionImageMatch>> matchesMap) {
		return matchesMap.values().stream().reduce(new ArrayList<>(), (target, elements) -> {
			target.addAll(elements);
			return target;
		});
	}

	private List<QuestionImage> extractQuestionImages(Map<Integer, List<QuestionImageMatch>> matchesMap) {
		return matchesMap.values().stream().map((List<QuestionImageMatch> value) -> {
			List<QuestionImage> collect = value.stream().map((QuestionImageMatch match) -> match.getQuestionImage())
					.collect(Collectors.toList());
			return collect;
		}).reduce(new ArrayList<>(), (target, elements) -> {
			target.addAll(elements);
			return target;
		});
	}
}
