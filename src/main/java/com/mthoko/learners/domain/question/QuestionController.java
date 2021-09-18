package com.mthoko.learners.domain.question;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.common.service.BaseService;
import com.mthoko.learners.domain.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("question")
public class QuestionController extends BaseController<Question> {

    private final QuestionService service;

    @Autowired
    public QuestionController(QuestionService service, CategoryService categoryService) {
        this.service = service;
    }

    @Override
    public BaseService<Question> getService() {
        return service;
    }

    @GetMapping("empty-choices")
    public List<Question> questionsWithNoChoices(@PathParam("category") String category) {
        return service.retrieveAll().stream()
                .filter(question -> question.getChoices() == null || question.getChoices().isEmpty())
                .collect(Collectors.toList());
    }

    @GetMapping("categoryName/{categoryName}")
    public List<Question> findByCategory(@PathVariable("categoryName") String categoryName) {
        return service.findByCategoryName(categoryName);
    }

    @GetMapping("categoryNumber/{categoryNumber}")
    public List<Question> findByCategoryNumber(@PathVariable("categoryNumber") Integer categoryNumber) {
        return service.findByCategoryNumber(categoryNumber);
    }

    @GetMapping("categoryNumber/{categoryNumber}/questionNumber/{questionNumber}")
    public Question findByCategory(@PathVariable("categoryNumber") Integer categoryNumber, @PathVariable("questionNumber") Integer questionNumber) {
        return service.findByCategoryNumberAndQuestionNumber(categoryNumber, questionNumber);
    }

    @GetMapping("category/{id}")
    public List<Question> findByCategoryId(@PathVariable("id") Long categoryId) {
        return service.findByCategoryId(categoryId);
    }

    @GetMapping("type/{type}")
    public List<Question> findByType(@PathVariable("type") Integer type) {
        return service.findByType(type);
    }

    @GetMapping("count/categoryId/{categoryId}")
    public long countByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return service.countByCategoryId(categoryId);
    }

    @GetMapping("count/categoryName/{categoryName}")
    public long countByCategoryName(@PathVariable("categoryName") String categoryName) {
        return service.countByCategoryName(categoryName);
    }

}
