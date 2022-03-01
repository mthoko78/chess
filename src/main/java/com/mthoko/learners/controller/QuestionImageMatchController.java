package com.mthoko.learners.controller;

import com.mthoko.learners.service.BaseService;
import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.persistence.entity.QuestionImageMatch;
import com.mthoko.learners.service.QuestionImageMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("question-image-match")
public class QuestionImageMatchController extends BaseController<QuestionImageMatch> {

	private QuestionImageMatchService service;

	@Autowired
	public QuestionImageMatchController(QuestionImageMatchService service) {
		this.service = service;
	}

	@Override
	public BaseService<QuestionImageMatch> getService() {
		return service;
	}

}
