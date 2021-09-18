package com.mthoko.learners.domain.choice;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.common.service.BaseService;
import com.mthoko.learners.domain.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("categoryName/{categoryName}")
    public List<Choice> findByCategoryName(@PathVariable("categoryName") String categoryName) {
        return service.findByCategoryName(categoryName);
    }
}
