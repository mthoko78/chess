package com.mthoko.mobile.domain.question.answer;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.common.controller.BaseController;
import com.mthoko.mobile.domain.category.CategoryService;
import com.mthoko.mobile.domain.question.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Map;

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
