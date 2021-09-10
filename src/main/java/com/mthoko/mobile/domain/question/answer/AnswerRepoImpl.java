package com.mthoko.mobile.domain.question.answer;

import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.category.CategoryServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnswerRepoImpl {

	private static CategoryServiceImpl categoryService = new CategoryServiceImpl();

	public Map<Integer, Answer> extractAnswers(Category category) {
		String path = "./docs/" + category.getName() + ".txt";
		int questionNum = 0;
		boolean processAnswers = false;
		Map<Integer, Answer> answers = new LinkedHashMap<>();
		for (String line : categoryService.getFileContents(path)) {
			if (line.length() == 0)
				continue;
			if (Character.isDigit(line.charAt(0))) {
				int num = Integer.parseInt(line.substring(0, line.indexOf(".")));
				if (!processAnswers && num < questionNum) {
					processAnswers = true;
				}
				questionNum = num;
				if (processAnswers) {
					String text = line.substring(line.indexOf(".") + 1).trim();
					final Answer answer = new Answer(new ArrayList<>());
					Arrays.asList(text.split(", ")).forEach((choice) -> answer.getSelection().add(choice.charAt(0)));
					answers.put(questionNum, answer);
				}

			}
		}

		return answers;
	}
}