package com.mthoko.mobile.domain.question;

import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.category.CategoryServiceImpl;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.mthoko.mobile.common.util.EntityUtil.getFileContents;

public class QuestionRepoImpl {

    public Map<Integer, Question> extractQuestions(Category category) {
        String path = "./docs/" + category.getName() + ".txt";
        int questionNum = 0;
        boolean processQuestions = true;
        Question currentQuestion = null;
        Map<Integer, Question> questions = new LinkedHashMap<>();
        CategoryServiceImpl categoryService = new CategoryServiceImpl();
        for (String line : getFileContents(path)) {
            if (line.length() == 0)
                continue;
            if (Character.isDigit(line.charAt(0))) {
                int num = Integer.parseInt(line.substring(0, line.indexOf(".")));
                if (processQuestions && num < questionNum) {
                    break;
                }
                questionNum = num;
                String text = line.substring(line.indexOf(".") + 1).trim();
                currentQuestion = new Question();
                currentQuestion.setNumber(questionNum);
                currentQuestion.setText(text);
                questions.put(questionNum, currentQuestion);
                category.setTotalQuestions(category.getTotalQuestions() + 1);
            }
        }
        return questions;
    }
}