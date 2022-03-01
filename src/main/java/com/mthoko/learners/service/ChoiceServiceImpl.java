package com.mthoko.learners.service;

import com.mthoko.learners.persistence.repository.ChoiceRepoImpl;
import com.mthoko.learners.persistence.entity.Category;
import com.mthoko.learners.persistence.entity.Choice;
import com.mthoko.learners.persistence.entity.Question;
import com.mthoko.learners.persistence.repository.ChoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mthoko.learners.common.util.EntityUtil.distinctCategories;
import static com.mthoko.learners.common.util.EntityUtil.filterQuestionsByCategory;

@Service
public class ChoiceServiceImpl extends BaseServiceImpl<Choice> implements ChoiceService {

    private final ChoiceRepo choiceRepo;

    private ChoiceRepoImpl choiceRepoImpl = new ChoiceRepoImpl();

    @Autowired
    public ChoiceServiceImpl(ChoiceRepo choiceRepo, ChoiceRepoImpl choiceRepoImpl) {
        this.choiceRepo = choiceRepo;
        this.choiceRepoImpl = choiceRepoImpl;
    }

    @Override
    public JpaRepository<Choice, Long> getRepo() {
        return choiceRepo;
    }

    @Override
    public List<Choice> findByQuestionNumberAndCategory(int questionNum, String category) {
        return choiceRepo.findByQuestionNumberAndCategory(questionNum, category);
    }

    @Override
    public List<Choice> findByCategoryName(String categoryName) {
        return choiceRepo.findByCategoryName(categoryName);
    }

    @Override
    public int countByCategoryName(String categoryName) {
        return choiceRepo.findByCategoryName(categoryName).size();
    }

    @Override
    public Map<Category, Map<Integer, List<Choice>>> populateChoices(List<Question> questions) {
        Map<Category, Map<Integer, List<Choice>>> choices = extractAllChoices(distinctCategories(questions));
        saveAll(choicesMapToList(choices));
        allocateChoicesToCategories(questions, choices);
        return choices;
    }

    private void allocateChoicesToCategories(List<Question> questions, Map<Category, Map<Integer, List<Choice>>> choices) {
        choices.entrySet().forEach(entry -> {
            Category category = entry.getKey();
            Map<Integer, List<Choice>> choicesByCategory = entry.getValue();
            List<Question> questionsByCategory = filterQuestionsByCategory(category, questions);
            questionsByCategory.forEach(question -> {
                if (choicesByCategory.containsKey(question.getNumber())) {
                    question.setChoices(choicesByCategory.get(question.getNumber()));
                } else {
                    question.setChoices(new ArrayList<>());
                }
            });
        });
    }

    private List<Choice> choicesMapToList(Map<Category, Map<Integer, List<Choice>>> choices) {
        return choices.values()
                .stream()
                .map(integerListMap -> integerListMap.values()).reduce(new ArrayList<>(), (lists, lists2) -> {
                    lists.addAll(lists2);
                    return lists;
                }).stream().reduce(new ArrayList<>(), (choices1, choices2) -> {
                    choices1.addAll(choices2);
                    return choices1;
                });
    }

    private Map<Category, Map<Integer, List<Choice>>> extractAllChoices(List<Category> categories) {
        Map<Category, Map<Integer, List<Choice>>> choices = new LinkedHashMap<>();
        for (Category category : categories) {
            if (!choices.containsKey(category)) {
                choices.put(category, new LinkedHashMap<>());
            }
            choices.get(category).putAll(choiceRepoImpl.extractChoices(category));
        }
        return choices;
    }

    @Override
    public List<Choice> findByCategoryId(Long id) {
        return choiceRepo.findByCategoryId(id);
    }
}
