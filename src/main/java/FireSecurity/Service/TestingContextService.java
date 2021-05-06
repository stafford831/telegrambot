package FireSecurity.Service;


import FireSecurity.Models.Question;

import java.util.List;

public interface TestingContextService {

    List<Question> getContext(Long tId);

    void putContext(Long tId, List<Question> context);

    void deleteContext(Long tId);
}
