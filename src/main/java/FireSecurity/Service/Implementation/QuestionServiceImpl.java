package FireSecurity.Service.Implementation;

import FireSecurity.InterLayer.QuestionDao;
import FireSecurity.Models.Question;
import FireSecurity.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
/**
 * реализация интерфейса QuestionService
 */
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    public List<Question> findAll() {
        return questionDao.findAll();
    }

}
