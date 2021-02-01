package com.mthoko.mobile.question;

import java.util.List;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.category.CategoryService;
import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.controller.BaseController;

@RestController
@RequestMapping("question")
public class QuestionController extends BaseController<Question> {

	private final QuestionService service;

	private final CategoryService categoryService;

	@Autowired
	public QuestionController(QuestionService service, CategoryService categoryService) {
		this.service = service;
		this.categoryService = categoryService;
	}

	@Override
	public BaseService<Question> getService() {
		return service;
	}

	@GetMapping("load")
	public List<Question> loadQuestions(@PathParam("category") String category) {
		return service.populateQuestionTable(categoryService.findByName(category).get());
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

	@GetMapping("category/{id}")
	public List<Question> findByCategoryId(@PathVariable("id") Long categoryId) {
		return service.findByCategoryId(categoryId);
	}

	@GetMapping("type/{type}")
	public List<Question> findByType(@PathVariable("type") Integer type) {
		return service.findByType(type);
	}

}
