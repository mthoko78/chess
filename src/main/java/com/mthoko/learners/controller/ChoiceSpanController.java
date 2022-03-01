package com.mthoko.learners.controller;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.service.BaseService;
import com.mthoko.learners.persistence.entity.ChoiceSpan;
import com.mthoko.learners.service.ChoiceSpanService;
import com.mthoko.learners.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
