package com.mthoko.mobile.domain.question.imagematch;

import com.mthoko.mobile.domain.question.Question;
import com.mthoko.mobile.domain.question.image.QuestionImage;

import java.util.*;

import static com.mthoko.mobile.common.util.MyConstants.QUESTIONS_IMAGES_PATH;
import static com.mthoko.mobile.domain.question.image.QuestionImageRepoImpl.*;

public class QuestionImageMatchRepoImpl {

    public ArrayList<QuestionImageMatch> findMatchesByQuestionId(Long questionId, int type) {
        ArrayList<QuestionImageMatch> matches = new ArrayList<QuestionImageMatch>();
        if (type == Question.TYPE_MATCHING) {
        }
        return matches;
    }

    public Map<Integer, List<QuestionImageMatch>> getQuestionMatches() {
        Set<Integer> large = largeImages();
        Set<Integer> unsquared = unsquaredImages();
        Map<Integer, List<QuestionImageMatch>> matches = new LinkedHashMap<>();
        for (String filename : filesList(QUESTIONS_IMAGES_PATH)) {
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