package com.mthoko.learners.domain.question;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.common.service.BaseServiceImpl;
import com.mthoko.learners.domain.category.Category;
import com.mthoko.learners.domain.category.CategoryRepo;
import com.mthoko.learners.domain.choice.Choice;
import com.mthoko.learners.domain.choice.ChoiceRepo;
import com.mthoko.learners.domain.choice.span.ChoiceSpan;
import com.mthoko.learners.domain.choice.span.ChoiceSpanRepo;
import com.mthoko.learners.domain.question.answer.Answer;
import com.mthoko.learners.domain.question.answer.AnswerRepo;
import com.mthoko.learners.domain.question.image.QuestionImage;
import com.mthoko.learners.domain.question.image.QuestionImageRepo;
import com.mthoko.learners.domain.question.image.QuestionImageRepoImpl;
import com.mthoko.learners.domain.question.image.QuestionImageServiceImpl;
import com.mthoko.learners.domain.question.imagematch.QuestionImageMatch;
import com.mthoko.learners.domain.question.imagematch.QuestionImageMatchRepo;
import com.mthoko.learners.domain.question.imagematch.QuestionImageMatchServiceImpl;
import com.mthoko.learners.exception.ApplicationException;
import com.mthoko.learners.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.mthoko.learners.common.util.EntityUtil.*;
import static com.mthoko.learners.common.util.MyConstants.DOCS;

@Service
public class QuestionServiceImpl extends BaseServiceImpl<Question> implements QuestionService {

    private final QuestionRepo questionRepo;

    private final AnswerRepo answerRepo;

    private final CategoryRepo categoryRepo;

    private final QuestionImageRepo questionImageRepo;

    private final QuestionRepoImpl questionRepoImpl = new QuestionRepoImpl();

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

    private List<Question> extractQuestions(Category category) {
        Map<Integer, Question> questionMap = questionRepoImpl.extractQuestions(category);
        ArrayList<Question> questions = new ArrayList<>(questionMap.values());
        questionMap.values().stream().forEach(q -> q.setCategory(category));
        QuestionImageServiceImpl questionImageService = new QuestionImageServiceImpl(null, new QuestionImageRepoImpl());
        QuestionImageMatchServiceImpl matchService = new QuestionImageMatchServiceImpl(null, null, null);
        Map<Integer, QuestionImage> questionImageMap = questionImageService.extractQuestionImages(category, questions);
        Map<Integer, List<QuestionImageMatch>> matches = matchService.extractImageMatches(category);
        questionMap.values()
                .stream()
                .forEach(q -> {
                    q.setImage(questionImageMap.get(q.getNumber()));
                    List<QuestionImageMatch> matchesForQuestion = matches.get(q.getNumber());
                    q.setMatches(matchesForQuestion != null ? matchesForQuestion : new ArrayList<>());
                });
        return questions;
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
    public Question save(Question question) {
        Question saved = questionRepo.save(saveDependencies(setDateBeforeSave(question, new Date())));
        rewriteQuestionsToFile(question.getCategory());
        return saved;
    }

    @Override
    @Transactional
    public List<Question> saveAll(List<Question> questions) {
        List<Category> categories = distinctCategories(questions);
        Date date = new Date();
        setDateBeforeSave(categories, date);
        setDateBeforeSave(questions, date);
        categoryRepo.saveAll(categories);
        questions.forEach(question -> {
            Category savedCategory = categories
                    .stream()
                    .filter(category -> category.equals(question.getCategory()))
                    .findFirst()
                    .get();
            question.setCategory(savedCategory);
        });
        saveDependencies(questions);
        List<Question> saved = questionRepo.saveAll(questions);
        categories.forEach(category -> rewriteQuestionsToFile(category));
        return saved;
    }

    @Override
    @Transactional
    public Question update(Question question) {
        Question saved = questionRepo.save(saveDependencies(setDateBeforeUpdate(question, new Date())));
        rewriteQuestionsToFile(question.getCategory());
        return saved;
    }

    @Override
    @Transactional
    public List<Question> updateAll(List<Question> questions) {
        setDateBeforeUpdate(questions, new Date());
        saveDependencies(questions);
        List<Question> saved = questionRepo.saveAll(questions);
        distinctCategories(questions).forEach(category -> rewriteQuestionsToFile(category));
        return saved;
    }

    private void saveDependencies(List<Question> questions) {
        choiceRepo.saveAll(extractAllChoices(questions));
        choiceSpanRepo.saveAll(extractAllChoiceSpans(questions));
        answerRepo.saveAll(extractAllAnswers(questions));
        List<QuestionImage> images = extractAllImages(questions);
        List<QuestionImageMatch> matches = extractAllMatches(questions);
        images.addAll(matches
                .stream()
                .map(questionImageMatch -> questionImageMatch.getQuestionImage())
                .collect(Collectors.toList()));
        List<QuestionImage> uniqueImages = images
                .stream()
                .distinct()
                .collect(Collectors.toList());
        questionImageRepo.saveAll(uniqueImages);
        questions
                .stream()
                .filter(question -> question.getImage() != null && question.getImage().getId() == null)
                .forEach(question -> {
                    QuestionImage image = uniqueImages
                            .stream()
                            .filter(questionImage -> question.getImage().equals(questionImage)).findFirst().get();
                    question.setImage(image);
                });
        questionImageMatchRepo.saveAll(matches);
    }

    private List<QuestionImageMatch> extractAllMatches(List<Question> questions) {
        return questions
                .stream()
                .map(question -> question.getMatches())
                .reduce(new ArrayList<>(), (questionImageMatches, questionImageMatches2) -> {
                    questionImageMatches.addAll(questionImageMatches2);
                    return questionImageMatches;
                });
    }

    private List<QuestionImage> extractAllImages(List<Question> questions) {
        return questions
                .stream()
                .map(question -> question.getImage())
                .filter(questionImage -> questionImage != null)
                .collect(Collectors.toList());
    }

    private List<Answer> extractAllAnswers(List<Question> questions) {
        return questions
                .stream()
                .map(question -> question.getAnswer())
                .collect(Collectors.toList());
    }

    private List<Choice> extractAllChoices(List<Question> questions) {
        return questions
                .stream()
                .map(question -> question.getChoices())
                .reduce(new ArrayList<>(), (choices1, choices2) -> {
                    choices1.addAll(choices2);
                    return choices1;
                });
    }

    private List<ChoiceSpan> extractAllChoiceSpans(List<Question> questions) {
        return questions
                .stream()
                .map(question -> question.getChoiceSpans())
                .reduce(new ArrayList<>(), (spans1, spans2) -> {
                    spans1.addAll(spans2);
                    return spans1;
                });
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
        super.setDateBeforeUpdate(entity, date);
        if (entity instanceof Question) {
            Question question = (Question) entity;
            super.setDateBeforeUpdate(question.getAnswer(), date);
            super.setDateBeforeUpdate(extractAllImages(question), date);
            super.setDateBeforeUpdate(question.getChoices(), date);
            super.setDateBeforeUpdate(question.getChoiceSpans(), date);
            super.setDateBeforeUpdate(question.getMatches(), date);
            super.setDateBeforeUpdate(question.getMatches()
                    .stream()
                    .map(questionImageMatch -> questionImageMatch.getQuestionImage())
                    .collect(Collectors.toList()), date
            );

        }
        return entity;
    }

    @Override
    public <V extends UniqueEntity> V setDateBeforeSave(V entity, Date date) {
        super.setDateBeforeSave(entity, date);
        if (entity instanceof Question) {
            Question question = (Question) entity;
            super.setDateBeforeSave(question.getAnswer(), date);
            super.setDateBeforeSave(question.getImage(), date);
            super.setDateBeforeSave(question.getChoices(), date);
            super.setDateBeforeSave(question.getChoiceSpans(), date);
            super.setDateBeforeSave(question.getMatches(), date);
            super.setDateBeforeSave(question.getMatches()
                    .stream()
                    .map(questionImageMatch -> questionImageMatch.getQuestionImage())
                    .collect(Collectors.toList()), date
            );
        }
        return entity;
    }

    private List<QuestionImage> extractAllImages(Question question) {
        return question.getMatches()
                .stream()
                .map(questionImageMatch -> questionImageMatch.getQuestionImage())
                .filter(questionImage -> questionImage != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Question> extractAllQuestions(List<Category> categories) {
        return categories
                .stream()
                .map(category -> extractQuestions(category))
                .reduce(new ArrayList<>(), (questions, questions2) -> {
                    questions.addAll(questions2);
                    return questions;
                });
    }

    @Override
    public Question findByCategoryNumberAndQuestionNumber(Integer categoryNumber, Integer questionNumber) {
        return questionRepo.findByCategory_NumberAndNumber(categoryNumber, questionNumber);
    }

    @Override
    public List<Question> findByCategoryNumber(Integer categoryNumber) {
        return questionRepo.findByCategory_Number(categoryNumber);
    }

    @Override
    public List<Question> rewriteQuestionsToFile(Category category) {
        List<Question> questions = findByCategoryId(category.getId());
        questions.sort((question, question2) -> question.getNumber() - question2.getNumber());
        String questionsToString = questionsToString(questions);
        Path path = Paths.get(DOCS + category.getName() + ".txt");
        try {
            Files.write(path, questionsToString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException(e, ErrorCode.FAILED_TO_WRITE_TEXT_TO_FILE);
        }
        return questions;
    }
}