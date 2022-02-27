package com.mthoko.learners.domain.question.answer;

import com.mthoko.learners.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Answer extends BaseEntity {

    @ElementCollection(targetClass = Character.class)
    private List<Character> selection;

    @Override
    public String getUniqueIdentifier() {
        String map = selection.stream().map((Function<Character, String>) (c) -> String.valueOf(c))
                .collect(Collectors.joining("|"));
        return map;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        Answer other = (Answer) obj;
        if (selection == null) {
            return other.selection == null;
        } else if (other.selection == null)
            return false;
        else if (selection.size() != other.selection.size()) {
            return false;
        }
        for (int i = 0; i < selection.size(); i++) {
            if (selection.get(i) != other.selection.get(i))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getSelection()
                .stream()
                .map(character -> character.toString().toUpperCase())
                .collect(Collectors.joining(", "));
    }

}
