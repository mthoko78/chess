package com.mthoko.mobile.question.image;

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
@RequestMapping("question-image")
public class QuestionImageController extends BaseController<QuestionImage> {

	private QuestionImageService service;

	private QuestionService questionService;

	private CategoryService categoryService;

	@Autowired
	public QuestionImageController(QuestionImageService service, QuestionService questionService,
			CategoryService categoryService) {
		this.service = service;
		this.questionService = questionService;
		this.categoryService = categoryService;
	}

	@Override
	public BaseService<QuestionImage> getService() {
		return service;
	}

	@GetMapping("load")
	public Map<Integer, QuestionImage> loadQuestionImages(@PathParam("category") String category) {
		return service.saveQuestionImages(categoryService.findByName(category).get(),
				questionService.findByCategoryName(category));
	}

}
