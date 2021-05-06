package FireSecurity.Service.Implementation;

import FireSecurity.Models.Question;
import FireSecurity.Service.TestingContextService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Getter
@RequiredArgsConstructor
public class TestsContextImpl implements TestingContextService {

    private final Map<Long, List<Question>> context;

    @Override
    public List<Question> getContext(Long tId) {
        return context.get(tId);
    }

    @Override
    public void putContext(Long tId, List<Question> ctx) {
        context.put(tId, ctx);
    }

    @Override
    public void deleteContext(Long tId) {
        context.remove(tId);
    }
}
