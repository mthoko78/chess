package com.mthoko.mobile.choice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mthoko.mobile.category.CategoryService;
import com.mthoko.mobile.common.BaseService;
import com.mthoko.mobile.controller.BaseController;

@RestController
@RequestMapping("choice")
public class ChoiceController extends BaseController<Choice> {

	private final ChoiceService service;

	private final CategoryService categoryService;

	@Autowired
	public ChoiceController(ChoiceService service, CategoryService categoryService) {
		this.service = service;
		this.categoryService = categoryService;
	}

	@Override
	public BaseService<Choice> getService() {
		return service;
	}

	@GetMapping("load")
	public Map<Integer, List<Choice>> loadChoices() {
		LinkedHashMap<Integer, List<Choice>> linkedHashMap = new LinkedHashMap<>();
		categoryService.retrieveAll().forEach((category) -> {
			linkedHashMap.putAll(service.saveChoices(category));
		});
		return linkedHashMap;
	}

	@GetMapping("categoryName/{categoryName}")
	public List<Choice> findByCategoryName(@PathVariable("categoryName") String categoryName) {
		return service.findByCategoryName(categoryName);
	}
}
