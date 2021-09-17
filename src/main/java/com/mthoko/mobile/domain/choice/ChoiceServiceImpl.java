package com.mthoko.mobile.domain.choice;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import com.mthoko.mobile.common.util.EntityUtil;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mthoko.mobile.common.util.EntityUtil.allocateChoicesToQuestions;

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
    public Map<Integer, List<Choice>> saveChoices(Category category) {
        Map<Integer, List<Choice>> choices = extractChoices(category);
        choiceRepo.saveAll(questionChoices(choices));
        return choices;
    }

    private Map<Integer, List<Choice>> extractChoices(Category category) {
        return choiceRepoImpl.extractChoices(category);
    }

    private List<Choice> questionChoices(Map<Integer, List<Choice>> choicesMap) {
        List<Choice> allChoices = choicesMap.values().stream().reduce(new ArrayList<>(), (target, elements) -> {
            target.addAll(elements);
            return target;
        });
        return allChoices;
    }

    @Override
    public int countByCategoryName(String categoryName) {
        return choiceRepo.findByCategoryName(categoryName).size();
    }

    @Override
    public Map<Category, Map<Integer, List<Choice>>> populateChoices(List<Question> questions) {
        List<Category> categories = EntityUtil.distinctCategories(questions);
        Map<Category, Map<Integer, List<Choice>>> choices = extractAllChoicesAsMap(categories);
        saveAll(choicesMapToList(choices));
        for (Category category : categories) {
            allocateChoicesToQuestions(category, filterQuestionsByCategory(questions, category), choices.get(category));
        }
        return choices;
    }

    private List<Choice> choicesMapToList(Map<Category, Map<Integer, List<Choice>>> choices) {
        List<Choice> allChoices = choices.values()
                .stream()
                .map(integerListMap -> questionChoices(integerListMap))
                .reduce((choices1, choices2) -> {
                    choices1.addAll(choices2);
                    return choices1;
                }).get();
        return allChoices;
    }

    private Map<Category, Map<Integer, List<Choice>>> extractAllChoicesAsMap(List<Category> categories) {
        Map<Category, Map<Integer, List<Choice>>> choices = new LinkedHashMap<>();
        for (Category category : categories) {
            Map<Integer, List<Choice>> choicesByCategory = extractChoices(category);
            if (!choices.containsKey(category)) {
                choices.put(category, new LinkedHashMap<>());
            }
            choices.get(category).putAll(choicesByCategory);
        }
        return choices;
    }

    private List<Question> filterQuestionsByCategory(List<Question> questions, Category category) {
        return questions.stream().filter(question -> question.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Choice> findByCategoryId(Long id) {
        return choiceRepo.findByCategoryId(id);
    }
}
