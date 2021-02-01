package com.mthoko.mobile.util;

import static com.mthoko.mobile.category.CategoryServiceImpl.ROAD_SIGNS_MARKINGS;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mthoko.mobile.category.CategoryServiceImpl;
import com.mthoko.mobile.choice.Choice;
import com.mthoko.mobile.choice.span.ChoiceSpan;
import com.mthoko.mobile.question.Question;
import com.mthoko.mobile.question.QuestionServiceImpl;
import com.mthoko.mobile.question.image.QuestionImage;
import com.mthoko.mobile.question.imagematch.QuestionImageMatch;

public class ETLModel {

	private static CategoryServiceImpl categoryService = new CategoryServiceImpl();

	private static QuestionServiceImpl questionService = new QuestionServiceImpl();

	public static void main(String[] args) throws IOException {

	}

	public static Map<Integer, Question> extractQuestions(String category) throws IOException {
		String path = "./docs/" + category + ".txt";
		int questionNum = 0;
		boolean processQuestions = true;
		Question currentQuestion = null;
		Map<Integer, Question> questions = new LinkedHashMap<>();
		for (String line : categoryService.getFileContents(path)) {
			if (line.length() == 0)
				continue;
			if (Character.isDigit(line.charAt(0))) {
				int num = Integer.parseInt(line.substring(0, line.indexOf(".")));
				if (processQuestions && num < questionNum) {
					processQuestions = false;
				}
				questionNum = num;
				String text = line.substring(line.indexOf(".") + 1).trim();
				if (processQuestions) {
					currentQuestion = new Question();
					currentQuestion.setNumber(questionNum);
					currentQuestion.setText(text);
					questions.put(questionNum, currentQuestion);
				} else {
					String[] choicesAsString = text.split(", ");
					List<String> asList = Arrays.asList(choicesAsString);
					final Question q = questions.get(questionNum);
					if (q != null) {
						asList.forEach((choice) -> q.addSelection(choice.charAt(0)));
					} else {
						throw new RuntimeException("No corresponding question number: " + questionNum);
					}
				}

			} else if (Character.isLetter(line.charAt(0))) {
				char letter = Character.toUpperCase(line.charAt(0));
				String val = line.substring(line.indexOf(")") + 1).trim();
				currentQuestion.getChoices().add(new Choice(letter, val));
			} else if ('(' == line.charAt(0)) {
				String romanFigure = line.substring(1, line.indexOf(")"));
				String val = line.substring(line.indexOf(")") + 1).trim();
				currentQuestion.getChoiceSpans().add(new ChoiceSpan(romanFigure, val));
			}
		}

		if (category.equals(ROAD_SIGNS_MARKINGS)) {
			Map<Integer, QuestionImage> questionImages = questionService.getQuestionImages();
			Map<Integer, List<QuestionImageMatch>> questionMatches = new LinkedHashMap<>();
			// questionService.getQuestionMatches();
			for (Map.Entry<Integer, Question> entry : questions.entrySet()) {
				if (questionImages.containsKey(entry.getKey())) {
					entry.getValue().setImage(questionImages.get(entry.getKey()));
				}
				if (questionMatches.containsKey(entry.getKey())) {
					entry.getValue().setMatches(questionMatches.get(entry.getKey()));
				}
			}
		}

		return questions;
	}

}