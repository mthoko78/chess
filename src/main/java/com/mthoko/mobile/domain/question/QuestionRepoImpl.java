package com.mthoko.mobile.domain.question;

import com.mthoko.mobile.domain.category.CategoryServiceImpl;

import java.util.LinkedHashMap;
import java.util.Map;

public class QuestionRepoImpl {

	public Map<Integer, Question> extractQuestions(String category) {
		String path = "./docs/" + category + ".txt";
		int questionNum = 0;
		boolean processQuestions = true;
		Question currentQuestion = null;
		Map<Integer, Question> questions = new LinkedHashMap<>();
		CategoryServiceImpl categoryService = new CategoryServiceImpl();
		for (String line : categoryService.getFileContents(path)) {
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

			}
		}
		return questions;
	}
}