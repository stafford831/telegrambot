package FireSecurity.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "test_results")
/**
 * запись результата
 */
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="completed_on")
    private Date completedOn;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="passed")
    private Boolean passed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult that = (TestResult) o;
        return Objects.equals(completedOn, that.completedOn) && user.equals(that.user) && passed.equals(that.passed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(completedOn, user, passed);
    }
}
