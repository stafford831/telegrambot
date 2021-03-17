package FireSecurity.Models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "questions")
/**
 * вопрос
 */
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // погуглить про GenerationType.IDENTITY
    private Long id;

    @Column(name="text")
    private String text;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) //погуглить про FetchType.EAGER
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Option> options = new ArrayList<>();

    @Transient
    private Long chosenOptionId = null;

    public boolean hasChosenOption() {
        return chosenOptionId != null;
    }

    public boolean isChosenOptionCorrect() {
        Optional<Option> opt = options.stream().filter(o -> o.getId().equals(chosenOptionId)).findAny();
        return opt.isPresent() ? opt.get().getCorrect() : false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return text.equals(question.text) && options.equals(question.options) && Objects.equals(chosenOptionId, question.chosenOptionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, options, chosenOptionId);
    }
}
