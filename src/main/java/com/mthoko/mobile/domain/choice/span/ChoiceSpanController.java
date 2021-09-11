package com.mthoko.mobile.domain.choice.span;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.common.controller.BaseController;
import com.mthoko.mobile.domain.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("choice-span")
public class ChoiceSpanController extends BaseController<ChoiceSpan> {

	private ChoiceSpanService service;

	private CategoryService categoryService;

	@Autowired
	public ChoiceSpanController(ChoiceSpanService service, CategoryService categoryService) {
		this.service = service;
		this.categoryService = categoryService;
	}

	@Override
	public BaseService<ChoiceSpan> getService() {
		return service;
	}

	@GetMapping("load/{categoryName}")
	public Map<Integer, List<ChoiceSpan>> loadChoiceSpans(@PathParam("category") String category) {
		return service.saveChoiceSpans(categoryService.findByName(category).get());
	}

}
