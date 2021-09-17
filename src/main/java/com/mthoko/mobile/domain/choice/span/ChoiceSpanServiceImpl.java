package com.mthoko.mobile.domain.choice.span;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.choice.ChoiceRepoImpl;
import com.mthoko.mobile.domain.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mthoko.mobile.common.util.EntityUtil.*;

@Service
public class ChoiceSpanServiceImpl extends BaseServiceImpl<ChoiceSpan> implements ChoiceSpanService {

    private final ChoiceSpanRepo choiceSpanRepo;

    private final ChoiceRepoImpl choiceRepoImpl = new ChoiceRepoImpl();

    @Autowired
    public ChoiceSpanServiceImpl(ChoiceSpanRepo choiceSpanRepo) {
        this.choiceSpanRepo = choiceSpanRepo;
    }

    @Override
    public JpaRepository<ChoiceSpan, Long> getRepo() {
        return choiceSpanRepo;
    }

    @Override
    public List<ChoiceSpan> findByQuestionNumberAndCategory(int questionNum, String category) {
        return choiceSpanRepo.findByQuestionNumberAndCategory(questionNum, category);
    }

    @Override
    public Map<Integer, List<ChoiceSpan>> saveChoiceSpans(Category category) {
        Map<Integer, List<ChoiceSpan>> choicesMap = extractChoiceSpans(category);
        saveAll(choiceSpansMapToList(choicesMap));
        return choicesMap;
    }

    private Map<Integer, List<ChoiceSpan>> extractChoiceSpans(Category category) {
        return choiceRepoImpl.extractChoiceSpans(category);
    }

    private List<ChoiceSpan> choiceSpansMapToList(Map<Integer, List<ChoiceSpan>> choicesMap) {
        List<ChoiceSpan> allSpans = choicesMap.values().stream().reduce(new ArrayList<>(),
                (target, elements) -> {
                    target.addAll(elements);
                    return target;
                });
        return allSpans;
    }

    @Override
    public Map<Category, Map<Integer, List<ChoiceSpan>>> populateChoiceSpans(List<Question> questions) {
        List<Category> categories = distinctCategories(questions);
        Map<Category, Map<Integer, List<ChoiceSpan>>> choiceSpans = allChoiceSpansAsMap(categories);
        saveAll(allChoiceSpansToList(choiceSpans));
        allocateToQuestions(questions, categories, choiceSpans);
        return choiceSpans;
    }

    private void allocateToQuestions(List<Question> questions, List<Category> categories, Map<Category, Map<Integer, List<ChoiceSpan>>> choiceSpans) {
        for (Category category : categories) {
            allocateChoiceSpansToQuestions(filterQuestionsByCategory(category, questions), choiceSpans.get(category));
        }
    }

    private List<ChoiceSpan> allChoiceSpansToList(Map<Category, Map<Integer, List<ChoiceSpan>>> choiceSpans) {
        List<ChoiceSpan> reduce = choiceSpans.values()
                .stream()
                .map(integerListMap -> choiceSpansMapToList(integerListMap))
                .reduce((choiceSpans1, choiceSpans2) -> {
                    choiceSpans1.addAll(choiceSpans2);
                    return choiceSpans1;
                }).get();
        return reduce;
    }

    private Map<Category, Map<Integer, List<ChoiceSpan>>> allChoiceSpansAsMap(List<Category> categories) {
        Map<Category, Map<Integer, List<ChoiceSpan>>> choices = new LinkedHashMap<>();
        for (Category category : categories) {
            if (!choices.containsKey(category)) {
                choices.put(category, new LinkedHashMap<>());
            }
            choices.get(category).putAll(extractChoiceSpans(category));
        }
        return choices;
    }

    @Override
    public List<ChoiceSpan> findByCategoryId(Long id) {
        return choiceSpanRepo.findByCategoryId(id);
    }
}
