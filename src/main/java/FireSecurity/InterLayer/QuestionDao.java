package FireSecurity.InterLayer;

import FireSecurity.Models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * погуглить про DAO
 */
@Repository
public interface QuestionDao extends JpaRepository<Question, Long> {
}
