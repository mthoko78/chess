package com.mthoko.learners.domain.question;

import com.mthoko.learners.common.entity.UniqueEntity;
import com.mthoko.learners.domain.category.Category;
import com.mthoko.learners.domain.choice.Choice;
import com.mthoko.learners.domain.choice.span.ChoiceSpan;
import com.mthoko.learners.domain.question.answer.Answer;
import com.mthoko.learners.domain.question.image.QuestionImage;
import com.mthoko.learners.domain.question.imagematch.QuestionImageMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
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
        StringBuilder builder = new StringBuilder(String.format("%s. %s", number, text));
        if (!choiceSpans.isEmpty()) {
            builder.append("\n").append(choiceSpans
                    .stream()
                    .map(choiceSpan -> choiceSpan.toString())
                    .collect(Collectors.joining("\n")));
        }
        if (!choices.isEmpty()) {
            builder.append("\n").append(this.choices
                    .stream()
                    .map(choice -> choice.toString())
                    .collect(Collectors.joining("\n")));
        }
        return builder.toString();
    }

}
