package com.mthoko.mobile.question.answer;

import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.category.CategoryService;
import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.controller.BaseController;
import com.mthoko.mobile.question.QuestionService;

@RestController
@RequestMapping("answer")
public class AnswerController extends BaseController<Answer> {

	private final AnswerService service;

	private final CategoryService categoryService;

	private final QuestionService questionService;

	@Autowired

	public AnswerController(AnswerService service, CategoryService categoryService, QuestionService questionService) {
		this.service = service;
		this.categoryService = categoryService;
		this.questionService = questionService;
	}

	@Override
	public BaseService<Answer> getService() {
		return service;
	}

	@GetMapping("load")
	public Map<Integer, Answer> loadAnswers(@PathParam("category") String category) {
		return service.saveAnswers(categoryService.findByName(category).get(),
				questionService.findByCategoryName(category));
	}

}
