package com.mthoko.learners.domain.question;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.common.service.BaseServiceImpl;
import com.mthoko.learners.domain.category.Category;
import com.mthoko.learners.domain.category.CategoryRepo;
import com.mthoko.learners.domain.choice.Choice;
import com.mthoko.learners.domain.choice.ChoiceRepo;
import com.mthoko.learners.domain.choice.span.ChoiceSpanRepo;
import com.mthoko.learners.domain.question.answer.Answer;
import com.mthoko.learners.domain.question.answer.AnswerRepo;
import com.mthoko.learners.domain.question.image.QuestionImage;
import com.mthoko.learners.domain.question.image.QuestionImageRepo;
import com.mthoko.learners.domain.question.image.QuestionImageRepoImpl;
import com.mthoko.learners.domain.question.imagematch.QuestionImageMatchRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mthoko.learners.common.util.EntityUtil.allocateAnswers;

@Service
public class QuestionServiceImpl extends BaseServiceImpl<Question> implements QuestionService {

    private final QuestionRepo questionRepo;

    private final AnswerRepo answerRepo;

    private final CategoryRepo categoryRepo;

    private final QuestionImageRepo questionImageRepo;

    private final QuestionRepoImpl questionRepoImpl = new QuestionRepoImpl();

    private final QuestionImageRepoImpl questionImageRepoImpl = new QuestionImageRepoImpl();

    private final ChoiceRepo choiceRepo;

    private final ChoiceSpanRepo choiceSpanRepo;

    private final QuestionImageMatchRepo questionImageMatchRepo;

    @Autowired
    public QuestionServiceImpl(QuestionRepo questionRepo,
                               AnswerRepo answerRepo, CategoryRepo categoryRepo, QuestionImageRepo questionImageRepo, ChoiceRepo choiceRepo, ChoiceSpanRepo choiceSpanRepo, QuestionImageMatchRepo questionImageMatchRepo) {
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
        this.categoryRepo = categoryRepo;
        this.questionImageRepo = questionImageRepo;
        this.choiceRepo = choiceRepo;
        this.choiceSpanRepo = choiceSpanRepo;
        this.questionImageMatchRepo = questionImageMatchRepo;
    }

    @Override
    public JpaRepository<Question, Long> getRepo() {
        return questionRepo;
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
        return questionImageRepoImpl.getQuestionSignImages();
    }

    @Override
    public List<Question> populateQuestionTable(Category category) {
        List<Question> existingQuestions = findByCategoryId(category.getId());
        if (!existingQuestions.isEmpty()) {
            return existingQuestions;
        }
        return saveAll(extractQuestions(category));
    }

    private List<Question> extractQuestions(Category category) {
        Collection<Question> questions = questionRepoImpl.extractQuestions(category).values();
        questions.stream().forEach(q -> q.setCategory(category));
        return new ArrayList<>(questions);
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
        return saveAll(extractAllQuestions(categories));
    }

    @Override
    public Question save(Question question) {
        return questionRepo.save(saveDependencies(setDateBeforeSave(question, new Date())));
    }

    @Override
    @Transactional
    public Question update(Question entity) {
        return questionRepo.save(saveDependencies(setDateBeforeUpdate(entity, new Date())));
    }

    private Question saveDependencies(Question question) {
        choiceRepo.saveAll(question.getChoices());
        choiceSpanRepo.saveAll(question.getChoiceSpans());
        answerRepo.save(question.getAnswer());
        questionImageRepo.saveAll(extractAllImages(question));
        questionImageMatchRepo.saveAll(question.getMatches());
        return question;
    }

    @Override
    public <V extends UniqueEntity> V setDateBeforeUpdate(V entity, Date date) {
        Question question = (Question) entity;
        super.setDateBeforeUpdate(question, date);
        super.setDateBeforeUpdate(question.getAnswer(), date);
        super.setDateBeforeUpdate(question.getMatches(), date);
        super.setDateBeforeUpdate(extractAllImages(question), date);
        super.setDateBeforeUpdate(question.getChoices(), date);
        super.setDateBeforeUpdate(question.getChoiceSpans(), date);
        return entity;
    }

    @Override
    public <V extends UniqueEntity> V setDateBeforeSave(V entity, Date date) {
        Question question = (Question) entity;
        super.setDateBeforeSave(question, date);
        super.setDateBeforeSave(question.getAnswer(), date);
        super.setDateBeforeSave(question.getMatches(), date);
        super.setDateBeforeSave(extractAllImages(question), date);
        super.setDateBeforeSave(question.getChoices(), date);
        super.setDateBeforeSave(question.getChoiceSpans(), date);
        return entity;
    }

    private List<QuestionImage> extractAllImages(Question updated) {
        List<QuestionImage> allImages = updated.getMatches()
                .stream()
                .map(questionImageMatch -> questionImageMatch.getQuestionImage())
                .collect(Collectors.toList());
        if (updated.getImage() != null) {
            allImages.add(updated.getImage());
        }
        return allImages;
    }

    @Override
    public List<Question> extractAllQuestions(List<Category> categories) {
        List<Question> allQuestions = categories
                .stream()
                .map(category -> extractQuestions(category))
                .reduce((questions, questions2) -> {
                    questions.addAll(questions2);
                    return questions;
                }).get();
        return allQuestions;
    }

    @Override
    public Question findByCategoryNumberAndQuestionNumber(Integer categoryNumber, Integer questionNumber) {
        return questionRepo.findByCategory_NumberAndNumber(categoryNumber, questionNumber);
    }

    @Override
    public List<Question> findByCategoryNumber(Integer categoryNumber) {
        return questionRepo.findByCategory_Number(categoryNumber);
    }
};