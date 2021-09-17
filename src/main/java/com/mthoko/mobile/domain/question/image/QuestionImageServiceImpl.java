package com.mthoko.mobile.domain.question.image;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.question.Question;
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

import static com.mthoko.mobile.common.util.EntityUtil.allocateImagesToQuestions;
import static com.mthoko.mobile.common.util.EntityUtil.distinctCategories;
import static com.mthoko.mobile.common.util.MyConstants.*;

@Service
public class QuestionImageServiceImpl extends BaseServiceImpl<QuestionImage> implements QuestionImageService {

    private final QuestionImageRepo imageRepo;

    private QuestionImageRepoImpl imageRepoImpl = new QuestionImageRepoImpl();

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
        questions = questions.stream().filter((question) -> category.equals(question.getCategory()))
                .collect(Collectors.toList());
        Map<Integer, QuestionImage> images = extractQuestionImages(category, questions);
        imageRepo.saveAll(new ArrayList<>(images.values()));
        return images;
    }

    private Map<Integer, QuestionImage> extractQuestionImages(Category category, List<Question> questions) {
        String categoryName = category.getName();
        questions = filterByCategoryName(questions, categoryName);
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
            Path path = Paths.get(QUESTIONS_IMAGES_PATH + "/" + image.getPath());
            if (Files.exists(path)) {
                return IOUtils.toByteArray(Files.newInputStream(path));
            }
        }
        return null;
    }

    @Override
    public Map<Category, Map<Integer, QuestionImage>> populateQuestionImages(List<Question> questions) {

        Map<Category, Map<Integer, QuestionImage>> images = new HashMap<>();
        for (Category category : distinctCategories(questions)) {
            if (!images.containsKey(category)) {
                images.put(category, new HashMap<>());
            }
            Map<Integer, QuestionImage> savedQuestionImages = saveQuestionImages(category, questions);
            images.get(category).putAll(savedQuestionImages);
            allocateImagesToQuestions(category, questions, savedQuestionImages);
        }
        return images;
    }
}
