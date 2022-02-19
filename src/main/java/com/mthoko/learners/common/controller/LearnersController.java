package com.mthoko.learners.common.controller;

import com.mthoko.learners.common.service.BaseService;
import com.mthoko.learners.domain.category.CategoryService;
import com.mthoko.learners.domain.choice.ChoiceService;
import com.mthoko.learners.domain.choice.span.ChoiceSpanService;
import com.mthoko.learners.domain.question.Question;
import com.mthoko.learners.domain.question.QuestionService;
import com.mthoko.learners.domain.question.answer.AnswerService;
import com.mthoko.learners.domain.question.image.QuestionImageService;
import com.mthoko.learners.domain.question.imagematch.QuestionImageMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return questionService.saveAll(questionService.extractAllQuestions(categoryService.getCategories()));
    }

    @Override
    public BaseService<Question> getService() {
        return questionService;
    }

}