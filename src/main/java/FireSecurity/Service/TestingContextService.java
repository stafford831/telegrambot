package FireSecurity.Service;


import FireSecurity.Models.Question;

import java.util.List;

/**
 * интерфейс для работы с текущим контекстом тестирования для конкретного пользователя, определяемого по телеграмм id
 */
public interface TestingContextService {

    List<Question> getContext(Long tId);

    void putContext(Long tId, List<Question> context);

    void deleteContext(Long tId);
}
