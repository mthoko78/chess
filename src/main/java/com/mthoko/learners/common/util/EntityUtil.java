package com.mthoko.learners.common.util;

import com.mthoko.learners.domain.category.Category;
import com.mthoko.learners.domain.choice.Choice;
import com.mthoko.learners.domain.choice.span.ChoiceSpan;
import com.mthoko.learners.domain.question.Question;
import com.mthoko.learners.domain.question.answer.Answer;
import com.mthoko.learners.domain.question.image.QuestionImage;
import com.mthoko.learners.exception.ApplicationException;
import com.mthoko.learners.exception.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.mthoko.learners.common.util.MyConstants.DOCS;

public class EntityUtil {

    public static void allocateChoicesToQuestions(Category category, List<Question> questions,
                                                  Map<Integer, List<Choice>> choicesMap) {
        List<Question> filtered = filterQuestionsByCategory(category, questions);
        for (Map.Entry<Integer, List<Choice>> entry : choicesMap.entrySet()) {
            Integer questionNum = entry.getKey();
            List<Choice> choices = entry.getValue();
            Optional<Question> optionalQuestion = filtered.stream()
                    .filter((question) -> question.getNumber() == questionNum).findFirst();
            if (!optionalQuestion.isPresent()) {
                throw new ApplicationException("No corresponding question to answer: " + choices);
            }
            optionalQuestion.get().setChoices(choices);
        }
    }

    public static List<Question> filterQuestionsByCategory(Category category, List<Question> questions) {
        return questions.stream().filter((question) -> question.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public static List<Category> distinctCategories(List<Question> questions) {
        return questions
                .stream()
                .map(question -> question.getCategory())
                .distinct()
                .collect(Collectors.toList());
    }

    public void allocateChoiceSpansToQuestions(Category category, List<Question> questions,
                                               Map<Integer, List<ChoiceSpan>> choiceSpanMap) {
        List<Question> filtered = filterQuestionsByCategory(category, questions);
        for (Map.Entry<Integer, List<ChoiceSpan>> entry : choiceSpanMap.entrySet()) {
            Integer questionNum = entry.getKey();
            List<ChoiceSpan> choiceSpans = entry.getValue();
            Optional<Question> optionalQuestion = filtered.stream()
                    .filter((question) -> question.getNumber() == questionNum).findFirst();
            if (!optionalQuestion.isPresent()) {
                throw new ApplicationException("No corresponding question to span: " + choiceSpans);
            }
            optionalQuestion.get().setChoiceSpans(choiceSpans);
        }
    }

    public static List<Question> allocateAnswers(List<Question> questions, List<Answer> answers) {
        questions.forEach((question) -> {
            Optional<Answer> first = answers.stream().filter((answer) -> answer.getId() == question.getId())
                    .findFirst();
            if (first.isPresent()) {
                question.setAnswer(first.get());
            }
        });
        return questions;
    }

    private List<QuestionImage> extractNonNullImages(List<Question> questions) {
        return questions.stream().map((question) -> question.getImage()).filter((image) -> image != null)
                .collect(Collectors.toList());
    }

    private List<Answer> extractNonNullAnswers(List<Question> questions) {
        return questions.stream().map((question) -> question.getAnswer()).filter((answer) -> answer != null)
                .collect(Collectors.toList());
    }

    public static void allocateChoiceSpansToQuestions(List<Question> questions, Map<Integer, List<ChoiceSpan>> choicesMap) {
        for (Map.Entry<Integer, List<ChoiceSpan>> entry : choicesMap.entrySet()) {
            Integer questionNum = entry.getKey();
            List<ChoiceSpan> choiceSpans = entry.getValue();
            Optional<Question> optionalQuestion = questions.stream()
                    .filter((question) -> question.getNumber() == questionNum).findFirst();
            if (!optionalQuestion.isPresent()) {
                throw new ApplicationException("No corresponding question to span: " + choiceSpans);
            }
            Question question = optionalQuestion.get();
            question.setChoiceSpans(choiceSpans);
        }
    }

    public static void allocateAnswersToQuestions(Category category, List<Question> questions,
                                                  Map<Integer, Answer> answersMap) {
        questions = questions.stream().filter(q -> category.equals(q.getCategory())).collect(Collectors.toList());
        for (Map.Entry<Integer, Answer> entry : answersMap.entrySet()) {
            Integer questionNum = entry.getKey();
            Answer answer = entry.getValue();
            Optional<Question> optionalQuestion = questions.stream()
                    .filter((question) -> question.getNumber() == questionNum).findFirst();
            if (!optionalQuestion.isPresent()) {
                throw new ApplicationException("No corresponding question to answer: " + answer);
            }
            optionalQuestion.get().setAnswer(answer);
        }
    }

    public static List<String> getFileContents(String path) {
//        return getContentsViaStream(path);
        return getContentsUsingNIO(path);
    }

    private static List<String> getContentsUsingNIO(String path) {
        Path path1 = Paths.get(path);
        if (!Files.exists(path1)) {
            throw new ApplicationException(ErrorCode.FILE_NOT_FOUND);
        }
        try {
            return Files.readAllLines(path1);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException(e, ErrorCode.FAILED_TO_READ_FILE_TEXT);
        }
    }

    private static List<String> getContentsViaStream(String path) {
        InputStream resource = EntityUtil.class.getClassLoader().getResourceAsStream(path);
        byte[] b;
        try {
            b = new byte[resource.available()];
            resource.read(b);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
        String contents = new String(b);
        String[] linesArray = contents.split("\r\n");
        List<String> lines = Arrays.asList(linesArray);
        return lines;
    }

    public static Map<Integer, Answer> extractAnswers(Category category) {
        int questionNum = 0;
        boolean processAnswers = false;
        Map<Integer, Answer> answers = new LinkedHashMap<>();
        for (String line : getFileContents(DOCS + category.getName() + ".txt")) {
            if (line.length() == 0)
                continue;
            if (Character.isDigit(line.charAt(0))) {
                int num = Integer.parseInt(line.substring(0, line.indexOf(".")));
                if (!processAnswers && num < questionNum) {
                    processAnswers = true;
                }
                questionNum = num;
                if (processAnswers) {
                    final Answer answer = extractAnswer(line);
                    answers.put(questionNum, answer);
                }

            }
        }

        return answers;
    }

    public static Answer extractAnswer(String line) {
        String text = line.substring(line.indexOf(".") + 1).trim();
        final Answer answer = new Answer(new ArrayList<>());
        Arrays.asList(text.split(", ")).forEach((choice) -> answer.getSelection().add(choice.charAt(0)));
        return answer;
    }

    public static void allocateImagesToQuestions(Category category, List<Question> questions,
                                                 Map<Integer, QuestionImage> images) {
        List<Question> filtered = questions.stream().filter(q -> category.equals(q.getCategory()))
                .collect(Collectors.toList());
        images.entrySet().forEach(entry -> {
            Optional<Question> match = filtered.stream().filter(q -> q.getNumber() == entry.getKey()).findFirst();
            if (match.isPresent()) {
                match.get().setImage(entry.getValue());
            }
        });
    }

    public static String questionsToString(List<Question> questions) {
        String questionsAsString = questions
                .stream()
                .map(question -> question.toString())
                .collect(Collectors.joining("\n\n"));
        String answersAsString = questions
                .stream()
                .map(question -> question.getNumber() + ". " + question.getAnswer().toString())
                .collect(Collectors.joining("\n"));
        return questionsAsString + "\n\n" + answersAsString;
    }

}
