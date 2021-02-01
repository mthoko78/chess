package com.mthoko.mobile.question.imagematch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.controller.BaseController;

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
