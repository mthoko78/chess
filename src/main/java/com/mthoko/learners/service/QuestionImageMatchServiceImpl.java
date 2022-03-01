package com.mthoko.learners.service;

import com.mthoko.learners.common.util.EntityUtil;
import com.mthoko.learners.persistence.repository.QuestionImageMatchRepoImpl;
import com.mthoko.learners.persistence.entity.Category;
import com.mthoko.learners.persistence.entity.Question;
import com.mthoko.learners.persistence.entity.QuestionImage;
import com.mthoko.learners.persistence.repository.QuestionImageMatchRepo;
import com.mthoko.learners.persistence.repository.QuestionImageRepo;
import com.mthoko.learners.persistence.entity.QuestionImageMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.mthoko.learners.common.util.EntityUtil.filterQuestionsByCategory;
import static com.mthoko.learners.common.util.MyConstants.ROAD_SIGNS_MARKINGS;

@Service
public class QuestionImageMatchServiceImpl extends BaseServiceImpl<QuestionImageMatch>
        implements QuestionImageMatchService {

    private final QuestionImageMatchRepo questionImageMatchRepo;

    private QuestionImageMatchRepoImpl imageMatchRepoImpl = new QuestionImageMatchRepoImpl();

    private QuestionImageService questionImageService;

    private final QuestionImageRepo questionImageRepo;

    @Autowired
    public QuestionImageMatchServiceImpl(QuestionImageMatchRepo imageMatchRepo, QuestionImageService imageRepo, QuestionImageRepo questionImageRepo) {
        this.questionImageMatchRepo = imageMatchRepo;
        this.questionImageService = imageRepo;
        this.questionImageRepo = questionImageRepo;
    }

    @Override
    public JpaRepository<QuestionImageMatch, Long> getRepo() {
        return questionImageMatchRepo;
    }

    @Override
    public Map<Category, Map<Integer, List<QuestionImageMatch>>> extractQuestionImageMatches(List<Question> questions) {
        Map<Category, Map<Integer, List<QuestionImageMatch>>> categoryMapMap = new HashMap<>();
        for (Category category : EntityUtil.distinctCategories(questions)) {
            if (!categoryMapMap.containsKey(category)) {
                categoryMapMap.put(category, new HashMap<>());
            }
            switch (category.getName()) {
                case ROAD_SIGNS_MARKINGS:
                    Map<Integer, List<QuestionImageMatch>> choicesMap = imageMatchRepoImpl.getQuestionMatches();
                    categoryMapMap.get(category).putAll(choicesMap);
                    break;
                default:
                    categoryMapMap.get(category).putAll(new HashMap<>());
            }
        }
        return categoryMapMap;
    }

    @Override
    public Map<Integer, List<QuestionImageMatch>> extractImageMatches(Category category) {
        switch (category.getName()) {
            case ROAD_SIGNS_MARKINGS:
                return imageMatchRepoImpl.getQuestionMatches();
            default:
                return new HashMap<>();
        }
    }

    @Override
    public void allocateMatchesToQuestions(List<Question> questions, Map<Category, Map<Integer, List<QuestionImageMatch>>> choicesMap) {
        for (Map.Entry<Category, Map<Integer, List<QuestionImageMatch>>> entry : choicesMap.entrySet()) {
            Category category = entry.getKey();
            Map<Integer, List<QuestionImageMatch>> matches = entry.getValue();
            List<Question> questionsByCategory = filterQuestionsByCategory(category, questions);
            for (Question question : questionsByCategory) {
                if (matches.containsKey(question.getNumber())) {
                    question.setMatches(matches.get(question.getNumber()));
                } else {
                    question.setMatches(new ArrayList<>());
                }
            }
        }
    }

    @Override
    public List<QuestionImageMatch> findByCategoryId(Long id) {
        return questionImageMatchRepo.findByCategoryId(id);
    }

    @Override
    public Map<Category, Map<Integer, List<QuestionImageMatch>>> populateQuestionImageMatches(List<Question> questions) {
        Map<Category, Map<Integer, List<QuestionImageMatch>>> matches = extractQuestionImageMatches(questions);
        List<QuestionImageMatch> allImageMatches = matchesMapToList(matches);
        allocateMatchesToQuestions(questions, matches);
        saveAll(allImageMatches);
        return matches;
    }

    @Override
    @Transactional
    public List<QuestionImageMatch> saveAll(List<QuestionImageMatch> questionImageMatches) {
        List<QuestionImage> questionImages = questionImageMatches
                .stream()
                .map(questionImageMatch -> questionImageMatch.getQuestionImage())
                .collect(Collectors.toList());
        questionImageRepo.saveAll(questionImages);
        return super.saveAll(questionImageMatches);
    }

    private List<QuestionImageMatch> matchesMapToList(Map<Category, Map<Integer, List<QuestionImageMatch>>> matches) {
        List<QuestionImageMatch> allImageMatches = matches.values()
                .stream()
                .map(integerListMap -> integerListMap.values()
                        .stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                .reduce(new ArrayList<>(), (questionImageMatches, questionImageMatches2) -> {
                    questionImageMatches.addAll(questionImageMatches2);
                    return questionImageMatches;
                });
        return allImageMatches;
    }

    private List<QuestionImageMatch> questionImageMatchesToList(Map<Integer, List<QuestionImageMatch>> matchesMap) {
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
