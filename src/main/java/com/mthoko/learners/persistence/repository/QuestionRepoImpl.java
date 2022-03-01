package com.mthoko.learners.persistence.repository;

import com.mthoko.learners.persistence.entity.Category;
import com.mthoko.learners.persistence.entity.Choice;
import com.mthoko.learners.persistence.entity.ChoiceSpan;
import com.mthoko.learners.persistence.entity.Question;

import java.util.*;

import static com.mthoko.learners.common.util.EntityUtil.extractAnswer;
import static com.mthoko.learners.common.util.EntityUtil.getFileContents;
import static com.mthoko.learners.common.util.MyConstants.DOCS;

public class QuestionRepoImpl {

    public static final String QUESTION_NUM = "questionNum";

    public Map<Integer, Question> extractQuestions(Category category) {
        Map<Integer, Question> questions = new LinkedHashMap<>();
        List<String> fileContents = getFileContents(DOCS + category.getName() + ".txt");
        AbstractMap.SimpleEntry<String, Integer> questionNo = new AbstractMap.SimpleEntry<>(QUESTION_NUM, null);
        fileContents
                .stream()
                .filter(line -> !line.trim().isEmpty()).forEach(
                        line -> {
                            if (isQuestionOrAnswer(line)) {
                                questionNo.setValue(questionNumber(line));
                                int number = questionNumber(line);
                                if (questions.containsKey(number)) {
                                    loadAnswer(questions, line, number);
                                } else {
                                    loadQuestion(category, questions, line);
                                }
                            } else if (Character.isLetter(line.charAt(0))) {
                                loadChoice(questions, questionNo.getValue(), line);
                            } else if ('(' == line.charAt(0)) {
                                loadChoiceSpan(questions, questionNo.getValue(), line);
                            }
                        }
                );



        return questions;
    }

    private void loadChoiceSpan(Map<Integer, Question> questions, Integer questionNo, String line) {
        String romanFigure = line.substring(1, line.indexOf(")"));
        String val = line.substring(line.indexOf(")") + 1).trim();
        ChoiceSpan choiceSpan = new ChoiceSpan(romanFigure, val);
        questions.get(questionNo).getChoiceSpans().add(choiceSpan);
    }

    private void loadChoice(Map<Integer, Question> questions, Integer questionNo, String line) {
        char letter = Character.toUpperCase(line.charAt(0));
        String val = line.substring(line.indexOf(")") + 1).trim();
        Choice choice = new Choice(letter, val);
        questions.get(questionNo).getChoices().add(choice);
    }

    private void loadAnswer(Map<Integer, Question> questions, String line, int questionNumber) {
        questions.get(questionNumber).setAnswer(extractAnswer(line));
    }

    private void loadQuestion(Category category, Map<Integer, Question> questions, String line) {
        Question question = new Question();
        question.setNumber(questionNumber(line));
        question.setText(line.substring(line.indexOf(".") + 1).trim());
        question.setChoices(new ArrayList<>());
        question.setChoiceSpans(new ArrayList<>());
        question.setMatches(new ArrayList<>());
        category.setTotalQuestions(category.getTotalQuestions() + 1);
        questions.put(question.getNumber(), question);
    }

    private boolean isQuestionOrAnswer(String line) {
        return !line.isEmpty() && Character.isDigit(line.charAt(0));
    }

    private int questionNumber(String line) {
        return Integer.parseInt(line.substring(0, line.indexOf(".")));
    }
}