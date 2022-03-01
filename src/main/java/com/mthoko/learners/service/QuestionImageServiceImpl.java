package com.mthoko.learners.service;

import com.mthoko.learners.persistence.repository.QuestionImageRepoImpl;
import com.mthoko.learners.persistence.entity.Category;
import com.mthoko.learners.persistence.entity.Question;
import com.mthoko.learners.persistence.entity.QuestionImage;
import com.mthoko.learners.persistence.repository.QuestionImageRepo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.mthoko.learners.common.util.EntityUtil.*;
import static com.mthoko.learners.common.util.MyConstants.*;

@Service
public class QuestionImageServiceImpl extends BaseServiceImpl<QuestionImage> implements QuestionImageService {

    private final QuestionImageRepo imageRepo;

    private final QuestionImageRepoImpl imageRepoImpl;

    @Autowired
    public QuestionImageServiceImpl(QuestionImageRepo imageRepo, QuestionImageRepoImpl imageRepoImpl) {
        this.imageRepo = imageRepo;
        this.imageRepoImpl = imageRepoImpl;
    }

    @Override
    public JpaRepository<QuestionImage, Long> getRepo() {
        return imageRepo;
    }

    @Override
    public List<QuestionImage> findByQuestionNumberAndCategory(int questionNum, String category) {
        return imageRepo.findByQuestionNumberAndCategory(questionNum, category);
    }

    @Override
    public Map<Integer, QuestionImage> saveQuestionImages(Category category, List<Question> questions) {
        Map<Integer, QuestionImage> integerQuestionImageMap = extractQuestionImages(category, questions);
        List<QuestionImage> images = integerQuestionImageMap.values()
                .stream()
                .collect(Collectors.toList());
        saveAll(images);
        return integerQuestionImageMap;
    }

    @Override
    public Map<Integer, QuestionImage> extractQuestionImages(Category category, List<Question> questions) {
        String categoryName = category.getName();
        questions = filterQuestionsByCategory(category, questions);
        Map<Integer, QuestionImage> images = new HashMap<>();
        switch (categoryName) {
            case ROAD_SIGNS_MARKINGS:
                Map<Integer, QuestionImage> choicesMap = imageRepoImpl.getQuestionSignImages();
                for (Entry<Integer, QuestionImage> entry : choicesMap.entrySet()) {
                    Integer questionNum = entry.getKey();
                    QuestionImage image = entry.getValue();
                    images.put(questionNum, image);
                }
                break;
            case RULES_OF_THE_ROAD:
                Map<Integer, QuestionImage> sketchImages = imageRepoImpl.getQuestionSketchImages();
                questions
                        .stream()
                        .filter(question -> question.getNumber() > 54)
                        .forEach(question -> {
                            QuestionImage image = sketchImages.get(question.getNumber());
                            question.setImage(image);
                            images.put(question.getNumber(), image);
                        });
                break;
            case HEAVY_MOTOR_VEHICLE_CONTROLS:
                QuestionImage heavyImage = imageRepoImpl.heavyControlsImage();
                questions.forEach(question -> {
                    if (question.getNumber() >= 6) {
                        question.setImage(heavyImage);
                        images.put(question.getNumber(), heavyImage);
                    }
                });
                break;
            case LIGHT_MOTOR_VEHICLE_CONTROLS:
                QuestionImage lightImage = imageRepoImpl.lightControlsImage();
                questions.forEach(question -> {
                    question.setImage(lightImage);
                    images.put(question.getNumber(), lightImage);
                });
                break;
        }
        return images;
    }

    private List<Question> filterByCategoryName(List<Question> questions, String categoryName) {
        return questions.stream().filter((question) -> categoryName.equals(question.getCategory().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionImage> findByCategoryId(Long id) {
        return imageRepo.findByCategoryId(id);
    }

    @Override
    public byte[] getImageAsBytes(Long imageId) throws IOException {
        Optional<QuestionImage> optionalQuestionImage = findById(imageId);
        if (optionalQuestionImage.isPresent()) {
            QuestionImage image = optionalQuestionImage.get();
            Path path = Paths.get(QUESTIONS_IMAGES_PATH + image.getPath());
            if (Files.exists(path)) {
                return IOUtils.toByteArray(Files.newInputStream(path));
            }
        }
        return null;
    }

    @Override
    public Map<Category, Map<Integer, QuestionImage>> populateQuestionImages(List<Question> questions) {
        List<Category> categories = distinctCategories(questions);
        Map<Category, Map<Integer, QuestionImage>> images = extractAllImages(questions, categories);
        saveAll(allImagesToList(images));
        for (Category category : categories) {
            allocateImagesToQuestions(category, questions, images.get(category));
        }
        return images;
    }

    @Override
    public Map<Category, Map<Integer, QuestionImage>> extractAllImages(List<Question> questions, List<Category> categories) {
        Map<Category, Map<Integer, QuestionImage>> images = new HashMap<>();
        for (Category category : categories) {
            if (!images.containsKey(category)) {
                images.put(category, new HashMap<>());
            }
            images.get(category).putAll(extractQuestionImages(category, questions));
        }
        return images;
    }

    private List<QuestionImage> allImagesToList(Map<Category, Map<Integer, QuestionImage>> images) {
        List<QuestionImage> allQuestionImages = images.values()
                .stream()
                .map(integerQuestionImageMap -> new ArrayList(integerQuestionImageMap.values()))
                .collect(Collectors.toList())
                .stream().reduce(new ArrayList(), (questionImages, questionImages2) -> {
                    questionImages.addAll(questionImages2);
                    return questionImages;
                });
        return allQuestionImages;
    }
}
