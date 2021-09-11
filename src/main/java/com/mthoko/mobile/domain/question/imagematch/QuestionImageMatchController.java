package com.mthoko.mobile.domain.question.imagematch;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.common.controller.BaseController;
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
