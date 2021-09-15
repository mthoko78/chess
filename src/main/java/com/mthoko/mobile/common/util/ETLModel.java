package com.mthoko.mobile.common.util;

import com.mthoko.mobile.domain.category.CategoryServiceImpl;
import com.mthoko.mobile.domain.choice.Choice;
import com.mthoko.mobile.domain.choice.span.ChoiceSpan;
import com.mthoko.mobile.domain.question.Question;
import com.mthoko.mobile.domain.question.QuestionServiceImpl;
import com.mthoko.mobile.domain.question.image.QuestionImage;
import com.mthoko.mobile.domain.question.imagematch.QuestionImageMatch;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mthoko.mobile.common.util.EntityUtil.getFileContents;
import static com.mthoko.mobile.domain.category.CategoryServiceImpl.ROAD_SIGNS_MARKINGS;

public class ETLModel {

    private static CategoryServiceImpl categoryService = new CategoryServiceImpl();

    private static QuestionServiceImpl questionService = new QuestionServiceImpl();

    public static void main(String[] args) throws IOException {

    }


}