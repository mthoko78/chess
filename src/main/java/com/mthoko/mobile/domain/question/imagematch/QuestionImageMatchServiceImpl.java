package com.mthoko.mobile.domain.question.imagematch;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.question.Question;
import com.mthoko.mobile.domain.question.image.QuestionImage;
import com.mthoko.mobile.domain.question.image.QuestionImageService;
import com.mthoko.mobile.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.mthoko.mobile.common.util.EntityUtil.distinctCategories;
import static com.mthoko.mobile.common.util.MyConstants.ROAD_SIGNS_MARKINGS;

@Service
public class QuestionImageMatchServiceImpl extends BaseServiceImpl<QuestionImageMatch>
        implements QuestionImageMatchService {

    private final QuestionImageMatchRepo questionImageMatchRepo;

    private QuestionImageMatchRepoImpl imageMatchRepoImpl = new QuestionImageMatchRepoImpl();

    private QuestionImageService questionImageService;

    @Autowired
    public QuestionImageMatchServiceImpl(QuestionImageMatchRepo imageMatchRepo, QuestionImageService imageRepo) {
        this.questionImageMatchRepo = imageMatchRepo;
        this.questionImageService = imageRepo;
    }

    @Override
    public JpaRepository<QuestionImageMatch, Long> getRepo() {
        return questionImageMatchRepo;
    }

    @Override
    public Map<Integer, List<QuestionImageMatch>> extractQuestionImageMatches(Category category,
                                                                              List<Question> questions) {
        questions = questions.stream().filter((q) -> category.equals(q.getCategory())).collect(Collectors.toList());
        Map<Integer, List<QuestionImageMatch>> images = new HashMap<>();
        switch (category.getName()) {
            case ROAD_SIGNS_MARKINGS:
                Map<Integer, List<QuestionImageMatch>> choicesMap = imageMatchRepoImpl.getQuestionMatches();
                images.putAll(choicesMap);
                allocateMatchesToQuestions(questions, choicesMap);
        }
        return images;
    }

    @Override
    public void allocateMatchesToQuestions(List<Question> questions,
                                           Map<Integer, List<QuestionImageMatch>> choicesMap) {
        for (Entry<Integer, List<QuestionImageMatch>> entry : choicesMap.entrySet()) {
            Integer questionNum = entry.getKey();
            List<QuestionImageMatch> matches = entry.getValue();
            Optional<Question> optional = questions.stream().filter((q) -> q.getNumber() == questionNum).findAny();
            if (!optional.isPresent()) {
                throw new ApplicationException("No corresponding question to match");
            }
            optional.get().setMatches(matches);
        }
    }

    @Override
    @Transactional
    public Map<Integer, List<QuestionImageMatch>> saveQuestionImageMatches(Category category,
                                                                           List<Question> questions) {
        if (findByCategoryId(category.getId()).size() > 0) {
            return new HashMap<>();
        }
        Map<Integer, List<QuestionImageMatch>> matchesMap = extractQuestionImageMatches(category, questions);
        List<QuestionImageMatch> matches = extractQuestionImageMatches(matchesMap);
        List<QuestionImage> questionImages = extractQuestionImages(matchesMap);
        questionImageService.saveAll(questionImages);
        saveAll(matches);
        return matchesMap;
    }

    @Override
    public List<QuestionImageMatch> findByCategoryId(Long id) {
        return questionImageMatchRepo.findByCategoryId(id);
    }

    @Override
    public Map<Integer, List<QuestionImageMatch>> populateQuestionImageMatches(List<Question> questions) {
        Map<Integer, List<QuestionImageMatch>> images = new HashMap<>();
        for (Category category : distinctCategories(questions)) {
            Map<Integer, List<QuestionImageMatch>> matches = saveQuestionImageMatches(category,
                    questions);
            images.putAll(matches);
        }
        return images;
    }

    private List<QuestionImageMatch> extractQuestionImageMatches(Map<Integer, List<QuestionImageMatch>> matchesMap) {
        return matchesMap.values().stream().reduce(new ArrayList<>(), (target, elements) -> {
            target.addAll(elements);
            return target;
        });
    }

    private List<QuestionImage> extractQuestionImages(Map<Integer, List<QuestionImageMatch>> matchesMap) {
        return matchesMap.values().stream().map((List<QuestionImageMatch> value) -> {
            List<QuestionImage> collect = value.stream().map((QuestionImageMatch match) -> match.getQuestionImage())
                    .collect(Collectors.toList());
            return collect;
        }).reduce(new ArrayList<>(), (target, elements) -> {
            target.addAll(elements);
            return target;
        });
    }
}
