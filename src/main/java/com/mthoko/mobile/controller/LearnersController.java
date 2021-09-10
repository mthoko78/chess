package com.mthoko.mobile.controller;

import com.mthoko.mobile.category.Category;
import com.mthoko.mobile.category.CategoryService;
import com.mthoko.mobile.choice.Choice;
import com.mthoko.mobile.choice.ChoiceService;
import com.mthoko.mobile.choice.span.ChoiceSpan;
import com.mthoko.mobile.choice.span.ChoiceSpanService;
import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.question.Question;
import com.mthoko.mobile.question.QuestionService;
import com.mthoko.mobile.question.answer.Answer;
import com.mthoko.mobile.question.answer.AnswerService;
import com.mthoko.mobile.question.image.QuestionImage;
import com.mthoko.mobile.question.image.QuestionImageService;
import com.mthoko.mobile.question.imagematch.QuestionImageMatch;
import com.mthoko.mobile.question.imagematch.QuestionImageMatchService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.mthoko.mobile.question.imagematch.QuestionImageMatchRepoImpl.IMAGES_TEST_QUESTIONS_PATH;

@RestController
@RequestMapping("learners")
public class LearnersController extends BaseController<Question> {

    private final CategoryService categoryService;

    private final QuestionService questionService;

    private final AnswerService answerService;

    private final ChoiceService choiceService;

    private final ChoiceSpanService choiceSpanService;

    private final QuestionImageService imageService;

    private final QuestionImageMatchService imageMatchService;

    @Autowired
    public LearnersController(CategoryService categoryService, QuestionService questionService,
                              AnswerService answerService, ChoiceService choiceService, ChoiceSpanService choiceSpanService,
                              QuestionImageService imageService, QuestionImageMatchService imageMatchService) {
        this.categoryService = categoryService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.choiceService = choiceService;
        this.choiceSpanService = choiceSpanService;
        this.imageService = imageService;
        this.imageMatchService = imageMatchService;
    }

    public static final int MINUTES = 60;
    public static final int HOURS = 0;
    public static final int TOTAL_SECONDS = HOURS * (60 * 60) + MINUTES * 60;
    public static final int SIGNS_TOTAL = 30;
    public static final int SIGNS_PASS_MARK = 23;
    public static final int RULES_TOTAL = 30;
    public static final int RULES_PASS_MARK = 22;
    public static final int LIGHT_CONTROLS_TOTAL = 8;
    public static final int LIGHT_CONTROLS_PASS_MARK = 6;
    public static final int HEAVY_CONTROLS_TOTAL = 8;
    public static final int CONTROLS_PASS_MARK = 6;

    @GetMapping("load")
    public List<Question> load() {
        List<Category> categories = populateCategories();
        List<Question> questions = populateQuestions(categories);
        populateChoices(categories, questions);
        populateChoiceSpans(categories, questions);
        populateAnswers(categories, questions);
        populateQuestionImages(categories, questions);
        populateQuestionImageMatches(categories, questions);
        questionService.saveAll(questions);
        return questions;
    }

    private List<Category> populateCategories() {
        List<Category> unsavedCategories = categoryService.getCategories();
        List<Category> savedCategories = categoryService.retrieveAll();
        unsavedCategories.removeAll(savedCategories);
        categoryService.saveAll(unsavedCategories);
        unsavedCategories.stream().filter((category) -> !savedCategories.contains(category))
                .forEach((category) -> savedCategories.add(category));
        return savedCategories;
    }

    private List<Question> populateQuestions(List<Category> categories) {
        List<Question> allQuestions = new ArrayList<>();
        for (Category category : categories) {
            List<Question> questions = questionService.populateQuestionTable(category);
            allQuestions.addAll(questions);
        }
        return allQuestions;
    }

    private Map<Category, Map<Integer, List<Choice>>> populateChoices(List<Category> categories,
                                                                      List<Question> questions) {
        Map<Category, Map<Integer, List<Choice>>> choices = new LinkedHashMap<>();
        for (Category category : categories) {
            Map<Integer, List<Choice>> savedChoices = choiceService.saveChoices(category);
            if (!choices.containsKey(category)) {
                choices.put(category, new LinkedHashMap<>());
            }
            choices.get(category).putAll(savedChoices);
            List<Question> byCat = questions.stream().filter(question -> question.getCategory().equals(category))
                    .collect(Collectors.toList());
            questionService.allocateChoicesToQuestions(category, byCat, savedChoices);
        }
        return choices;
    }

    private Map<Category, Map<Integer, List<ChoiceSpan>>> populateChoiceSpans(List<Category> categories,
                                                                              List<Question> questions) {
        Map<Category, Map<Integer, List<ChoiceSpan>>> choices = new LinkedHashMap<>();
        for (Category category : categories) {
            Map<Integer, List<ChoiceSpan>> saved = choiceSpanService.saveChoiceSpans(category);
            if (!choices.containsKey(category)) {
                choices.put(category, new LinkedHashMap<>());
            }
            choices.get(category).putAll(saved);
            List<Question> byCat = questions.stream().filter(question -> question.getCategory().equals(category))
                    .collect(Collectors.toList());
            choiceSpanService.allocateChoiceSpansToQuestions(byCat, saved);
        }
        return choices;
    }

    private Map<Category, Map<Integer, Answer>> populateAnswers(List<Category> categories, List<Question> questions) {
        Map<Category, Map<Integer, Answer>> answers = new HashMap<>();
        if (answerService.count() == 0) {
            for (Category category : categories) {
                Map<Integer, Answer> savedAnswers = answerService.saveAnswers(category, questions);
                answers.put(category, savedAnswers);
                answerService.allocateAnswersToQuestions(category, questions, savedAnswers);
            }
        }
        return answers;
    }

    private Map<Category, Map<Integer, QuestionImage>> populateQuestionImages(List<Category> categories,
                                                                              List<Question> questions) {
        Map<Category, Map<Integer, QuestionImage>> images = new HashMap<>();
        for (Category category : categories) {
            if (!images.containsKey(category)) {
                images.put(category, new HashMap<>());
            }
            Map<Integer, QuestionImage> savedQuestionImages = imageService.saveQuestionImages(category, questions);
            images.get(category).putAll(savedQuestionImages);
            imageService.allocateImagesToQuestions(category, questions, savedQuestionImages);
        }
        return images;
    }

    private Map<Integer, List<QuestionImageMatch>> populateQuestionImageMatches(List<Category> categories,
                                                                                List<Question> questions) {
        Map<Integer, List<QuestionImageMatch>> images = new HashMap<>();
        for (Category category : categories) {
            Map<Integer, List<QuestionImageMatch>> matches = imageMatchService.saveQuestionImageMatches(category,
                    questions);
            images.putAll(matches);
        }
        return images;
    }

    @Override
    public BaseService<Question> getService() {
        return questionService;
    }

    @GetMapping(value = "image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable("id") Long imageId) throws IOException {
        QuestionImage image = imageService.findById(imageId);
        String path = image.getPath();
        String relativePath = IMAGES_TEST_QUESTIONS_PATH + "/" + path;
        InputStream in = getClass().getClassLoader().getResourceAsStream(relativePath);
        return IOUtils.toByteArray(in);
    }

}