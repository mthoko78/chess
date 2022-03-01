package com.mthoko.learners.service;

import com.mthoko.learners.persistence.entity.Category;
import com.mthoko.learners.persistence.repository.ChoiceRepoImpl;
import com.mthoko.learners.persistence.entity.ChoiceSpan;
import com.mthoko.learners.persistence.entity.Question;
import com.mthoko.learners.persistence.repository.ChoiceSpanRepo;
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

    private Map<Integer, List<ChoiceSpan>> extractChoiceSpans(Category category) {
        return choiceRepoImpl.extractChoiceSpans(category);
    }

    @Override
    public Map<Category, Map<Integer, List<ChoiceSpan>>> populateChoiceSpans(List<Question> questions) {
        Map<Category, Map<Integer, List<ChoiceSpan>>> choiceSpans = allChoiceSpans(distinctCategories(questions));
        saveAll(allChoiceSpansToList(choiceSpans));
        allocateChoiceSpansToQuestions(questions, choiceSpans);
        return choiceSpans;
    }


    private void allocateChoiceSpansToQuestions(List<Question> questions, Map<Category, Map<Integer, List<ChoiceSpan>>> choices) {
        choices.entrySet().forEach(entry -> {
            Category category = entry.getKey();
            Map<Integer, List<ChoiceSpan>> choicesByCategory = entry.getValue();
            List<Question> questionsByCategory = filterQuestionsByCategory(category, questions);
            questionsByCategory.forEach(question -> {
                if (choicesByCategory.containsKey(question.getNumber())) {
                    question.setChoiceSpans(choicesByCategory.get(question.getNumber()));
                } else {
                    question.setChoices(new ArrayList<>());
                }
            });
        });
    }

    private List<ChoiceSpan> allChoiceSpansToList(Map<Category, Map<Integer, List<ChoiceSpan>>> choiceSpans) {
        List<ChoiceSpan> reduce = choiceSpans.values()
                .stream()
                .map(integerListMap -> integerListMap.values()
                        .stream()
                        .reduce(new ArrayList<>(), (target, elements) -> {
                            target.addAll(elements);
                            return target;
                        }))
                .reduce(new ArrayList<>(),(choiceSpans1, choiceSpans2) -> {
                    choiceSpans1.addAll(choiceSpans2);
                    return choiceSpans1;
                });
        return reduce;
    }

    private Map<Category, Map<Integer, List<ChoiceSpan>>> allChoiceSpans(List<Category> categories) {
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
