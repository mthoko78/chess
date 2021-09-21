package com.mthoko.learners.domain.choice;

import com.mthoko.learners.domain.category.Category;
import com.mthoko.learners.domain.category.CategoryServiceImpl;
import com.mthoko.learners.domain.choice.span.ChoiceSpan;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mthoko.learners.common.util.EntityUtil.getFileContents;
import static com.mthoko.learners.common.util.MyConstants.DOCS;

public class ChoiceRepoImpl {

    private static CategoryServiceImpl categoryService = new CategoryServiceImpl();

    public Map<Integer, List<Choice>> extractChoices(Category category) {
        int questionNum = 0;
        Map<Integer, List<Choice>> choices = new LinkedHashMap<>();
        for (String line : getFileContents(DOCS + category.getName() + ".txt")) {
            if (line.length() == 0)
                continue;
            if (Character.isDigit(line.charAt(0))) {
                int num = Integer.parseInt(line.substring(0, line.indexOf(".")));
                if (num < questionNum) {
                    break;
                }
                questionNum = num;
                choices.put(questionNum, new ArrayList<>());
                continue;
            } else if (Character.isLetter(line.charAt(0))) {
                char letter = Character.toUpperCase(line.charAt(0));
                String val = line.substring(line.indexOf(")") + 1).trim();
                Choice choice = new Choice(letter, val);
                choices.get(questionNum).add(choice);
            }
        }
        return choices;
    }

    public Map<Integer, List<ChoiceSpan>> extractChoiceSpans(Category category) {
        int questionNum = 0;
        Map<Integer, List<ChoiceSpan>> choiceSpans = new LinkedHashMap<>();
        for (String line : getFileContents(DOCS + category.getName() + ".txt")) {
            if (line.length() == 0)
                continue;
            if (Character.isDigit(line.charAt(0))) {
                int num = Integer.parseInt(line.substring(0, line.indexOf(".")));
                if (num < questionNum) {
                    break;
                }
                questionNum = num;
                choiceSpans.put(questionNum, new ArrayList<>());
                continue;
            } else if ('(' == line.charAt(0)) {
                String romanFigure = line.substring(1, line.indexOf(")"));
                String val = line.substring(line.indexOf(")") + 1).trim();
                choiceSpans.get(questionNum).add(new ChoiceSpan(romanFigure, val));
            }
        }
        return choiceSpans;
    }
}