package com.mthoko.mobile.domain.question.image;

import java.io.File;
import java.net.URL;
import java.util.*;

import static com.mthoko.mobile.common.util.MyConstants.IMAGES_TEST_QUESTIONS;

public class QuestionImageRepoImpl {

    public QuestionImage findByQuestionId(Long id) {
        QuestionImage image = new QuestionImage();
        return image;
    }

    public Map<Integer, QuestionImage> getQuestionSignImages() {
        String path = IMAGES_TEST_QUESTIONS;
        Set<Integer> large = largeImages();
        Set<Integer> unsquared = unsquaredImages();
        Map<Integer, QuestionImage> questionImages = new LinkedHashMap<>();
        for (String filename : filesList(path)) {
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
        String path = IMAGES_TEST_QUESTIONS;
        Map<Integer, QuestionImage> questionImages = new LinkedHashMap<>();
        Integer[] largeDimensions = new Integer[]{450, 320};
        Integer[] extraLarge = new Integer[]{550, 400};
        for (String filename : filesList(path)) {
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
        String path = IMAGES_TEST_QUESTIONS + "/heavy_controls.png";
        return new QuestionImage(path.substring(path.lastIndexOf('/') + 1), 550, 400);
    }

    public QuestionImage lightControlsImage() {
        String path = IMAGES_TEST_QUESTIONS + "/light_controls.png";
        return new QuestionImage(path.substring(path.lastIndexOf('/') + 1), 500, 500);
    }

    public static String[] filesList(String dirName) {
        URL resource = QuestionImage.class.getClassLoader().getResource(dirName);
        File dir = new File(resource.getFile());
        String[] filenames = dir.list();
        return filenames;
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