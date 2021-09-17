package com.mthoko.mobile.domain.choice.span;

import com.mthoko.mobile.common.service.BaseServiceImpl;
import com.mthoko.mobile.domain.category.Category;
import com.mthoko.mobile.domain.choice.ChoiceRepoImpl;
import com.mthoko.mobile.domain.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.mthoko.mobile.common.util.EntityUtil.allocateChoiceSpansToQuestions;
import static com.mthoko.mobile.common.util.EntityUtil.distinctCategories;

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
        List<ChoiceSpan> existingChoiceSpans = findByCategoryId(category.getId());
        if (!existingChoiceSpans.isEmpty()) {
            return new HashMap<>();
        }
        Map<Integer, List<ChoiceSpan>> choicesMap = choiceRepoImpl.extractChoiceSpans(category);

        List<ChoiceSpan> allSpans = choicesMap.values().stream().reduce(new ArrayList<ChoiceSpan>(),
                (target, elements) -> {
                    target.addAll(elements);
                    return target;
                });
        saveAll(allSpans);
        return choicesMap;
    }

    @Override
    public Map<Category, Map<Integer, List<ChoiceSpan>>> populateChoiceSpans(List<Question> questions) {
        Map<Category, Map<Integer, List<ChoiceSpan>>> choices = new LinkedHashMap<>();
        for (Category category : distinctCategories(questions)) {
            Map<Integer, List<ChoiceSpan>> saved = saveChoiceSpans(category);
            if (!choices.containsKey(category)) {
                choices.put(category, new LinkedHashMap<>());
            }
            choices.get(category).putAll(saved);
            List<Question> byCat = questions.stream().filter(question -> question.getCategory().equals(category))
                    .collect(Collectors.toList());
            allocateChoiceSpansToQuestions(byCat, saved);
        }
        return choices;
    }

    @Override
    public List<ChoiceSpan> findByCategoryId(Long id) {
        return choiceSpanRepo.findByCategoryId(id);
    }
}
