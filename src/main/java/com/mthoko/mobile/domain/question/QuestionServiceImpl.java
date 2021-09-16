package com.mthoko.mobile.domain.question;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.category.CategoryRepo;
import com.mthoko.mobile.domain.choice.Choice;
import com.mthoko.mobile.domain.question.answer.Answer;
import com.mthoko.mobile.domain.question.answer.AnswerRepo;
import com.mthoko.mobile.domain.question.image.QuestionImage;
import com.mthoko.mobile.domain.question.image.QuestionImageRepo;
import com.mthoko.mobile.domain.question.image.QuestionImageRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mthoko.mobile.common.util.EntityUtil.allocateAnswers;

@Service
public class QuestionServiceImpl extends BaseServiceImpl<Question> implements QuestionService {

    private final QuestionRepo questionRepo;

    private final AnswerRepo answerRepo;

    private final CategoryRepo categoryRepo;

    private final QuestionImageRepo imageRepo;

//	private final QuestionImageMatchRepoImpl matchRepoImpl = new QuestionImageMatchRepoImpl();

    private final QuestionRepoImpl questionRepoImpl = new QuestionRepoImpl();

    private final QuestionImageRepoImpl questionImageRepo = new QuestionImageRepoImpl();

    @Autowired
    public QuestionServiceImpl(QuestionRepo questionRepo,
                               AnswerRepo answerRepo, CategoryRepo categoryRepo, QuestionImageRepo imageRepo) {
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
        this.categoryRepo = categoryRepo;
        this.imageRepo = imageRepo;
    }

    public QuestionServiceImpl() {
        this(null, null, null, null);
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
    public Optional<Question> findById(Long id) {
        return questionRepo.findById(id);
    }

    @Override
    public void setQuestionType(Question question) {
        if (question.getAnswer() != null) {
            if (question.getAnswer().getSelection().size() == 1) {
                question.setType(Question.TYPE_ONE_ANSWER);
            } else {
                question.setType(Question.TYPE_MATCHING);
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

    @Override
    public List<Question> findByCategoryName(String category) {
        return questionRepo.findByCategoryName(category);
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

    @Override
    public List<Question> populateQuestions(List<Category> categories) {
        List<Question> allQuestions = new ArrayList<>();
        for (Category category : categories) {
            List<Question> questions = populateQuestionTable(category);
            category.setTotalQuestions(questions.size());
            allQuestions.addAll(questions);
        }
        categoryRepo.saveAll(categories);
        return allQuestions;
    }

    @Override
    public Question findByCategoryNumberAndQuestionNumber(Integer categoryNumber, Integer questionNumber) {
        return questionRepo.findByCategory_NumberAndNumber(categoryNumber, questionNumber);
    }
};