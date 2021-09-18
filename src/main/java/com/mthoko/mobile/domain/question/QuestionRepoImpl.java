package com.mthoko.mobile.domain.question;

import com.mthoko.mobile.domain.category.Category;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mthoko.mobile.common.util.EntityUtil.getFileContents;
import static com.mthoko.mobile.common.util.MyConstants.DOCS;

public class QuestionRepoImpl {

    public Map<Integer, Question> extractQuestions(Category category) {
        Map<Integer, Question> questions = new LinkedHashMap<>();
        List<String> fileContents = getFileContents(DOCS + category.getName() + ".txt");
        fileContents
                .stream()
                .filter(line -> isQuestion(line) && !questions.containsKey(questionNumber(line))).forEach(
                        line -> {
                            Question question = new Question();
                            question.setNumber(questionNumber(line));
                            question.setText(line.substring(line.indexOf(".") + 1).trim());
                            category.setTotalQuestions(category.getTotalQuestions() + 1);
                            questions.put(question.getNumber(), question);
                        }
                );
        return questions;
    }

    private boolean isQuestion(String line) {
        return !line.isEmpty() && Character.isDigit(line.charAt(0));
    }

    private int questionNumber(String line) {
        return Integer.parseInt(line.substring(0, line.indexOf(".")));
    }
}