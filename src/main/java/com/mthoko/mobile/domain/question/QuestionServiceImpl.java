package com.mthoko.mobile.domain.question;

import com.mthoko.mobile.common.BaseServiceImpl;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.category.CategoryRepo;
import com.mthoko.mobile.domain.choice.Choice;
import com.mthoko.mobile.domain.choice.ChoiceRepo;
import com.mthoko.mobile.domain.choice.span.ChoiceSpan;
import com.mthoko.mobile.domain.choice.span.ChoiceSpanRepo;
import com.mthoko.mobile.domain.question.answer.Answer;
import com.mthoko.mobile.domain.question.answer.AnswerRepo;
import com.mthoko.mobile.domain.question.image.QuestionImage;
import com.mthoko.mobile.domain.question.image.QuestionImageRepo;
import com.mthoko.mobile.domain.question.image.QuestionImageRepoImpl;
import com.mthoko.mobile.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuestionServiceImpl extends BaseServiceImpl<Question> implements QuestionService {

	private final QuestionRepo questionRepo;

	private final ChoiceRepo choiceRepo;

	private final ChoiceSpanRepo choiceSpanRepo;

	private final AnswerRepo answerRepo;

	private final CategoryRepo categoryRepo;

	private final QuestionImageRepo imageRepo;

//	private final QuestionImageMatchRepoImpl matchRepoImpl = new QuestionImageMatchRepoImpl();

	private final QuestionRepoImpl questionRepoImpl = new QuestionRepoImpl();

	private final QuestionImageRepoImpl questionImageRepo = new QuestionImageRepoImpl();

	@Autowired
	public QuestionServiceImpl(QuestionRepo questionRepo, ChoiceRepo choiceRepo, ChoiceSpanRepo choiceSpanRepo,
			AnswerRepo answerRepo, CategoryRepo categoryRepo, QuestionImageRepo imageRepo) {
		this.questionRepo = questionRepo;
		this.choiceRepo = choiceRepo;
		this.choiceSpanRepo = choiceSpanRepo;
		this.answerRepo = answerRepo;
		this.categoryRepo = categoryRepo;
		this.imageRepo = imageRepo;
	}

	public QuestionServiceImpl() {
		this(null, null, null, null, null, null);
	}

	@Override
	public JpaRepository<Question, Long> getRepo() {
		return questionRepo;
	}

	@Override
	public QuestionImage findByQuestionId(Long id) {
		return questionImageRepo.findByQuestionId(id);
	}

	@Override
	public Question findById(Long id) {
		Optional<Question> question = questionRepo.findById(id);
		if (question.isPresent()) {
			return question.get();
		}
		return null;
	}

	@Override
	public void setQuestionType(Question question) {
		if (question.getAnswer() != null) {
			if (question.getAnswer().getSelection().size() == 1) {
				question.setType(Question.ONE_ANSWER);
			} else {
				question.setType(Question.MATCHING);
			}
		}
	}

	@Override
	public Question findByQuestionNumberAndCategory(int questionNum, String category) {
		Optional<Question> question = questionRepo.findByNumberAndCategoryName(questionNum, category);
		if (question.isPresent()) {
			return question.get();
		}
		return null;
	}

	public Map<Long, Question> getRandomQuestions(String category, int total, Long offset) {
		Optional<Category> optionalCategory = categoryRepo.findByName(category);
		if (optionalCategory.isPresent()) {
			return getRandomQuestions(optionalCategory.get(), total, offset);
		}
		return null;
	}

	public Map<Long, Question> getRandomQuestions(Category category, int total, Long offset) {
		ArrayList<Integer> list = getRandomList(category.getTotalQuestions());
		if (total > list.size())
			total = list.size();
		Map<Long, Question> result = new HashMap<>();
		for (int i = 0; i < total; i++) {
			result.put(i + offset, this.findByQuestionNumberAndCategory(list.get(i), category.getName()));
		}
		return result;
	}

	public static boolean hasDuplicates(HashMap<Integer, Question> questions) {
		LinkedHashSet<Question> set = new LinkedHashSet<>();
		for (Question q : questions.values()) {
			set.add(q);
		}
		return set.size() != questions.size();
	}

	public static ArrayList<Integer> getRandomList(int total) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i <= total; i++) {
			list.add(i);
		}
		ArrayList<Integer> newList = new ArrayList<Integer>();
		while (!list.isEmpty()) {
			int ran = (int) (Math.random() * list.size());
			newList.add(list.get(ran));
			list.remove(ran);
		}
		return newList;
	}

	public Optional<Category> findCategoryByName(String string) {
		return categoryRepo.findByName(string);
	}

	public Optional<Category> findCategoryById(long categoryId) {
		return categoryRepo.findById(categoryId);
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

	public Map<Integer, QuestionImage> getQuestionImages() {
		return questionImageRepo.getQuestionSignImages();
	}

	public Map<Integer, Question> extractQuestions(String name) {
		return questionRepoImpl.extractQuestions(name);
	}

	@Override
	public List<Question> populateQuestionTable(Category category) {
		List<Question> existingQuestions = findByCategoryId(category.getId());
		if (!existingQuestions.isEmpty()) {
			return existingQuestions;
		}
		String categoryName = category.getName();
		List<Question> questions = new ArrayList<>(questionRepoImpl.extractQuestions(categoryName).values());
		for (Question question : questions) {
			question.setCategory(category);
		}
		questions.removeAll(existingQuestions);
		if (!questions.isEmpty()) {
			saveAll(questions);
		}
		List<Question> collect = questions.stream().filter((q) -> !existingQuestions.contains(q))
				.collect(Collectors.toList());
		existingQuestions.addAll(collect);
		return existingQuestions;
	}

	@Override
	public List<Answer> findAnswersByCategoryId(Long id) {
		return answerRepo.findByCategoryId(id);
	}

	@Override
	public List<Question> findByCategoryId(Long id) {
		return allocateAnswers(questionRepo.findByCategoryId(id), answerRepo.findByCategoryId(id));
	}

	private List<Question> allocateAnswers(List<Question> questions, List<Answer> answers) {
		questions.forEach((question) -> {
			Optional<Answer> first = answers.stream().filter((answer) -> answer.getId() == question.getId())
					.findFirst();
			if (first.isPresent()) {
				question.setAnswer(first.get());
			}
		});
		return questions;
	}

	private List<QuestionImage> extractNonNullImages(List<Question> questions) {
		return questions.stream().map((question) -> question.getImage()).filter((image) -> image != null)
				.collect(Collectors.toList());
	}

	private List<Answer> extractNonNullAnswers(List<Question> questions) {
		return questions.stream().map((question) -> question.getAnswer()).filter((answer) -> answer != null)
				.collect(Collectors.toList());
	}

	@Override
	public List<Question> findByCategoryName(String category) {
		return questionRepo.findByCategoryName(category);
	}

	@Override
	public void allocateChoicesToQuestions(Category category, List<Question> questions,
			Map<Integer, List<Choice>> choicesMap) {
		List<Question> filtered = filterQuestionsByCategory(category, questions);
		for (Entry<Integer, List<Choice>> entry : choicesMap.entrySet()) {
			Integer questionNum = entry.getKey();
			List<Choice> choices = entry.getValue();
			Optional<Question> optionalQuestion = filtered.stream()
					.filter((question) -> question.getNumber() == questionNum).findFirst();
			if (!optionalQuestion.isPresent()) {
				throw new ApplicationException("No corresponding question to answer: " + choices);
			}
			optionalQuestion.get().setChoices(choices);
		}
	}

	private List<Question> filterQuestionsByCategory(Category category, List<Question> questions) {
		return questions.stream().filter((question) -> question.getCategory().equals(category))
				.collect(Collectors.toList());
	}

	@Override
	public void allocateChoiceSpansToQuestions(Category category, List<Question> questions,
			Map<Integer, List<ChoiceSpan>> choiceSpanMap) {
		List<Question> filtered = filterQuestionsByCategory(category, questions);
		for (Entry<Integer, List<ChoiceSpan>> entry : choiceSpanMap.entrySet()) {
			Integer questionNum = entry.getKey();
			List<ChoiceSpan> choiceSpans = entry.getValue();
			Optional<Question> optionalQuestion = filtered.stream()
					.filter((question) -> question.getNumber() == questionNum).findFirst();
			if (!optionalQuestion.isPresent()) {
				throw new ApplicationException("No corresponding question to span: " + choiceSpans);
			}
			optionalQuestion.get().setChoiceSpans(choiceSpans);
		}
	}

	@Override
	public List<Question> findByType(Integer type) {
		return questionRepo.findByType(type);
	}

	@Override
	public long countByCategoryId(Long categoryId) {
		return questionRepo.countByCategory_Id(categoryId);
	}

	@Override
	public long countByCategoryName(String categoryName) {
		return questionRepo.countByCategory_Name(categoryName);
	}
};