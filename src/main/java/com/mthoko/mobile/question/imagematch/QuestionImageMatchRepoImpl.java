package com.mthoko.mobile.question.imagematch;

import static com.mthoko.mobile.question.image.QuestionImageRepoImpl.filesList;
import static com.mthoko.mobile.question.image.QuestionImageRepoImpl.imageDimensions;
import static com.mthoko.mobile.question.image.QuestionImageRepoImpl.largeImages;
import static com.mthoko.mobile.question.image.QuestionImageRepoImpl.unsquaredImages;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mthoko.mobile.question.Question;
import com.mthoko.mobile.question.image.QuestionImage;

public class QuestionImageMatchRepoImpl {

	public ArrayList<QuestionImageMatch> findMatchesByQuestionId(Long questionId, int type) {
		ArrayList<QuestionImageMatch> matches = new ArrayList<QuestionImageMatch>();
		if (type == Question.MATCHING) {
		}
		return matches;
	}

	public Map<Integer, List<QuestionImageMatch>> getQuestionMatches() {
		String path = "./images/test/questions";
		Set<Integer> large = largeImages();
		Set<Integer> unsquared = unsquaredImages();
		Map<Integer, List<QuestionImageMatch>> matches = new LinkedHashMap<>();
		for (String filename : filesList(path)) {
			if (filename.startsWith("q_")) {
				String substring = filename.substring(2, filename.indexOf("."));
				if (substring.contains("_")) {
					String[] tokens = substring.split("_");
					int questionNumber = Integer.parseInt(tokens[0]);
					int imageNumber = Integer.parseInt(tokens[1]);
					if (!matches.containsKey(questionNumber)) {
						matches.put(questionNumber, new ArrayList<>());
					}
					Integer[] imageDimensions = imageDimensions(questionNumber, unsquared, large);
					QuestionImage image = new QuestionImage(filename, imageDimensions[0], imageDimensions[1]);
					matches.get(questionNumber).add(new QuestionImageMatch(imageNumber, image));
				}
			}
		}
		return matches;
	}
}