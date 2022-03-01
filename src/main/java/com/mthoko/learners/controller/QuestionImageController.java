package com.mthoko.learners.controller;

import com.mthoko.learners.common.controller.BaseController;
import com.mthoko.learners.service.BaseService;
import com.mthoko.learners.persistence.entity.QuestionImage;
import com.mthoko.learners.service.QuestionImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("question-image")
public class QuestionImageController extends BaseController<QuestionImage> {

    private QuestionImageService service;

    @Autowired
    public QuestionImageController(QuestionImageService service) {
        this.service = service;
    }

    @Override
    public BaseService<QuestionImage> getService() {
        return service;
    }

    @GetMapping(value = "image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable("id") Long imageId) throws IOException {
        return service.getImageAsBytes(imageId);
    }

}
