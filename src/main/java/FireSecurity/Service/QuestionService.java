package FireSecurity.Service;

import FireSecurity.Models.Question;

import java.util.List;

/**
 * интерфейс для работы с вопросами
 */
public interface QuestionService {

    List<Question> findAll();
}
