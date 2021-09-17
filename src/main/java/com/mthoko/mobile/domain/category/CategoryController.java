package com.mthoko.mobile.domain.category;

import com.mthoko.mobile.common.service.BaseService;
import com.mthoko.mobile.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("category")
public class CategoryController extends BaseController<Category> {

    @Autowired
    private CategoryService service;

    @Override
    public BaseService<Category> getService() {
        return service;
    }

    @GetMapping("name/{name}")
    public Optional<Category> findByName(@PathVariable("name") String name) {
        return service.findByName(name);
    }

    @GetMapping("names")
    public List<String> retrieveAllCategoryNames() {
        return service.retrieveAllNames();
    }

}
