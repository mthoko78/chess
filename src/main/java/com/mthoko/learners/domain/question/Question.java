package com.mthoko.learners.domain.question;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.domain.category.Category;
import com.mthoko.learners.domain.choice.Choice;
import com.mthoko.learners.domain.choice.span.ChoiceSpan;
import com.mthoko.learners.domain.question.answer.Answer;
import com.mthoko.learners.domain.question.image.QuestionImage;
import com.mthoko.learners.domain.question.imagematch.QuestionImageMatch;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mthoko
 */
@Entity
public final class Question extends UniqueEntity {

    public static final int TYPE_ONE_ANSWER = 0;
    public static final int TYPE_CHOICE = 1;
    public static final int TYPE_MATCHING = 2;

    private int number;

    private int type;

    private String text;

    @OneToOne
    private Category category;

    @OneToOne
    private QuestionImage image;

    @OneToOne
    private Answer answer;

    @OneToMany
    private List<Choice> choices = new ArrayList<>();

    @OneToMany
    private List<ChoiceSpan> choiceSpans = new ArrayList<>();

    @OneToMany
    private List<QuestionImageMatch> matches = new ArrayList<>();

    public Question() {
    }

    public Question(int number, int type, String text, Category category, QuestionImage image, Answer answer,
                    List<Choice> choices, List<ChoiceSpan> choiceSpans, List<QuestionImageMatch> matches) {
        this.number = number;
        this.type = type;
        this.text = text;
        this.category = category;
        this.image = image;
        this.answer = answer;
        this.choices = choices;
        this.choiceSpans = choiceSpans;
        this.matches = matches;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public List<ChoiceSpan> getChoiceSpans() {
        return choiceSpans;
    }

    public void setChoiceSpans(List<ChoiceSpan> choiceSpans) {
        this.choiceSpans = choiceSpans;
    }

    public int getTotalAnswers() {
        return choices.size();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<QuestionImageMatch> getMatches() {
        return matches;
    }

    public QuestionImage getImage() {
        return image;
    }

    public void setImage(QuestionImage image) {
        this.image = image;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setMatches(List<QuestionImageMatch> matches) {
        this.matches = matches;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
        if (answer != null) {
            if (answer.getSelection().size() == 1) {
                setType(Question.TYPE_ONE_ANSWER);
            } else {
                setType(Question.TYPE_MATCHING);
            }
        }
    }

    @Override
    public String getUniqueIdentifier() {
        return (category != null ? category.getName() : null) + "|" + number + "|" + text;
    }

    public static List<String> formatString(String s, int maxLen) {
        List<String> result = new ArrayList<String>();
        if (s.length() <= maxLen) {
            result.add(s);
            return result;
        } else {
            int i = 0;
            String[] tokens = s.split(" ");
            if (tokens.length < 2) {
                result.add(s);
                return result;
            }
            String newS = tokens[i];
            while ((newS + " " + tokens[i + 1]).length() <= maxLen) {
                newS += " " + tokens[++i];
                if (i + 1 >= tokens.length) {
                    break;
                }
            }
            result.add(newS);
            if (i + 1 >= tokens.length)
                return result;
            else {
                String nextLine = tokens[++i];
                for (i = i + 1; i < tokens.length; i++) {
                    nextLine += " " + tokens[i];
                }
                result.addAll(formatString(nextLine, maxLen));
                return result;
                // result.add(nextLine);
            }

        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass())
            return false;
        Question other = (Question) obj;
        if (category == null) {
            if (other.category != null)
                return false;
        } else if (!category.equals(other.category))
            return false;
        return (number == other.number);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result += prime * result + number;
        return result;
    }

    @Override
    public String toString() {
        return "Question [number=" + number + ", type=" + type + ", text=" + text + ", category=" + category
                + ", image=" + image + ", answer=" + answer + ", choices=" + choices + ", choiceSpans=" + choiceSpans
                + ", matches=" + matches + ", id=" + getId() + "]";
    }

    public void addSelection(char letter) {
        if (answer == null) {
            answer = new Answer(new ArrayList<>());
        }
        if (!answer.getSelection().contains(letter)) {
            answer.getSelection().add(letter);
        }
    }

}
