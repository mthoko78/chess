package com.mthoko.mobile.domain.question.image;

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
