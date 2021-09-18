package com.mthoko.learners.domain.question.image;

import com.mthoko.learners.exception.ApplicationException;
import com.mthoko.learners.exception.ErrorCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.mthoko.learners.common.util.MyConstants.QUESTIONS_IMAGES_PATH;

public class QuestionImageRepoImpl {

    public QuestionImage findByQuestionId(Long id) {
        QuestionImage image = new QuestionImage();
        return image;
    }

    public Map<Integer, QuestionImage> getQuestionSignImages() {
        Set<Integer> large = largeImages();
        Set<Integer> unsquared = unsquaredImages();
        Map<Integer, QuestionImage> questionImages = new LinkedHashMap<>();
        for (String filename : filesList(QUESTIONS_IMAGES_PATH)) {
            if (filename.startsWith("q_")) {
                String substring = filename.substring(2, filename.indexOf("."));
                if (!substring.contains("_")) {
                    int questionNumber = Integer.parseInt(substring);
                    Integer[] imageDimensions = imageDimensions(questionNumber, unsquared, large);
                    QuestionImage image = new QuestionImage(filename, imageDimensions[0], imageDimensions[1]);
                    questionImages.put(questionNumber, image);
                }
            }
        }
        return questionImages;
    }

    public Map<Integer, QuestionImage> getQuestionSketchImages() {
        Map<Integer, QuestionImage> questionImages = new LinkedHashMap<>();
        Integer[] largeDimensions = new Integer[]{450, 320};
        Integer[] extraLarge = new Integer[]{550, 400};
        for (String filename : filesList(QUESTIONS_IMAGES_PATH)) {
            if (filename.startsWith("sketch_")) {
                String substring = filename.substring("sketch_".length(), filename.indexOf("."));
                String[] tokens = substring.split("_");
                String[] questionNumTokens = substring.substring(substring.indexOf('_') + 1).split("_");
                int sketchNo = Integer.parseInt(tokens[0]);
                for (String string : questionNumTokens) {
                    int questionNumber = Integer.parseInt(string);
                    Integer[] imageDimensions = sketchNo < 8 ? largeDimensions : extraLarge;
                    QuestionImage image = new QuestionImage(filename, imageDimensions[0], imageDimensions[1]);
                    Optional<QuestionImage> first = questionImages.values()
                            .stream()
                            .filter(img -> img.getPath().equals(image.getPath())).findFirst();
                    if (first.isPresent()) {
                        questionImages.put(questionNumber, first.get());
                    } else {
                        questionImages.put(questionNumber, image);
                    }
                }
            }
        }
        return questionImages;
    }

    public QuestionImage heavyControlsImage() {
        return new QuestionImage("heavy_controls.png", 550, 400);
    }

    public QuestionImage lightControlsImage() {
        return new QuestionImage("light_controls.png", 500, 500);
    }

    public static List<String> filesList(String dirName) {
        try {
            return Files.list(Paths.get(dirName + "/"))
                    .map(path1 -> path1.toFile().getName())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException(ErrorCode.DIRECTORY_DOES_NOT_EXIST);
        }
    }

    public static Integer[] imageDimensions(int qNum, Set<Integer> unsquared, Set<Integer> large) {
        int w, h;
        if (qNum > 9 && !(unsquared.contains(qNum))) {
            w = h = 120;
        } else if (large.contains(qNum)) {
            w = 450;
            h = 320;
        } else {
            w = 210;
            h = 120;
        }
        return new Integer[]{w, h};
    }

    public static HashSet<Integer> largeImages() {
        HashSet<Integer> large = new HashSet<Integer>();
        large.add(1);
        large.add(2);
        large.add(3);
        large.add(4);
        large.add(92);
        large.add(93);
        large.add(96);
        large.add(97);
        return large;
    }

    public static HashSet<Integer> unsquaredImages() {
        HashSet<Integer> unsquared = new HashSet<Integer>();
        unsquared.add(12);
        unsquared.add(13);
        unsquared.add(16);
        unsquared.add(86);
        unsquared.add(92);
        unsquared.add(93);
        unsquared.add(95);
        unsquared.add(96);
        unsquared.add(97);
        return unsquared;
    }
}