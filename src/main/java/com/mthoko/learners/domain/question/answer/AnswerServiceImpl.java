package com.mthoko.learners.domain.question.answer;

import com.mthoko.learners.common.service.BaseServiceImpl;
import com.mthoko.learners.domain.category.Category;
import com.mthoko.learners.domain.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.mthoko.learners.common.util.EntityUtil.*;

@Service
public class AnswerServiceImpl extends BaseServiceImpl<Answer> implements AnswerService {

    @Autowired
    private AnswerRepo answerRepo;

    private AnswerRepoImpl answerRepoImpl = new AnswerRepoImpl();

    @Override
    public JpaRepository<Answer, Long> getRepo() {
        return answerRepo;
    }

    @Override
    public Optional<Answer> findByQuestionNumberAndCategory(int questionNum, String category) {
        return answerRepo.findByQuestionNumberAndCategory(questionNum, category);
    }

    @Override
    public Map<Integer, Answer> saveAnswers(Category category, List<Question> questions) {
        questions.stream().filter((question) -> category.equals(question.getCategory()))
                .collect(Collectors.toList());
        List<Answer> existingAnswers = findByCategoryId(category.getId());
        if (!existingAnswers.isEmpty()) {
            return new HashMap<>();
        }
        Map<Integer, Answer> answersMap = extractAnswers(category);
        saveAll(new ArrayList<>(answersMap.values()));
        return answersMap;
    }

    @Override
    public Map<Category, Map<Integer, Answer>> populateAnswers(List<Question> questions) {
        Map<Category, Map<Integer, Answer>> answers = new HashMap<>();
        if (count() == 0) {
            for (Category category : distinctCategories(questions)) {
                Map<Integer, Answer> savedAnswers = saveAnswers(category, questions);
                answers.put(category, savedAnswers);
                allocateAnswersToQuestions(category, questions, savedAnswers);
            }
        }
        return answers;
    }

    @Override
    public List<Answer> findAnswersByCategoryId(Long id) {
        return answerRepo.findByCategoryId(id);
    }

    @Override
    public List<Answer> findByCategoryId(Long id) {
        return answerRepo.findByCategoryId(id);
    }

}
