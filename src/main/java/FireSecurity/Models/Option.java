package FireSecurity.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "options")
/**
 * вариант ответов
 */
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name="correct")
    private Boolean correct;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return text.equals(option.text) && question.equals(option.question) && correct.equals(option.correct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, question, correct);
    }
}
