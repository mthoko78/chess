package com.mthoko.learners.controller;

import com.mthoko.learners.service.BaseService;
import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.persistence.entity.Answer;
import com.mthoko.learners.service.AnswerService;
import com.mthoko.learners.service.CategoryService;
import com.mthoko.learners.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
