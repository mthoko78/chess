package com.mthoko.mobile.common.controller;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.category.CategoryService;
import com.mthoko.mobile.domain.choice.ChoiceService;
import com.mthoko.mobile.domain.choice.span.ChoiceSpanService;
import com.mthoko.mobile.domain.question.Question;
import com.mthoko.mobile.domain.question.QuestionService;
import com.mthoko.mobile.domain.question.answer.AnswerService;
import com.mthoko.mobile.domain.question.image.QuestionImageService;
import com.mthoko.mobile.domain.question.imagematch.QuestionImageMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

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
        if (categoryService.count() == 0) {
            load();
        }
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
        List<Category> categories = categoryService.populateCategories();
        List<Question> questions = questionService.extractAllQuestions(categories);
        choiceService.populateChoices(questions);
        choiceSpanService.populateChoiceSpans(questions);
        answerService.populateAnswers(questions);
        imageService.populateQuestionImages(questions);
        imageMatchService.populateQuestionImageMatches(questions);
        questionService.saveAll(questions);
        return questions;
    }

    @Override
    public BaseService<Question> getService() {
        return questionService;
    }

    @GetMapping(value = "image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable("id") Long imageId) throws IOException {
        return imageService.getImageAsBytes(imageId);
    }

}