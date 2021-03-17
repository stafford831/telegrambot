package FireSecurity.Handlers;

import FireSecurity.Models.Question;
import FireSecurity.Models.User;
import FireSecurity.Service.QuestionService;
import FireSecurity.Service.TestingContextService;
import FireSecurity.Service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component("/test")
/**
 * класс, который обрабатывает команду начала тестирования
 */
public class TestHandler extends TextHandler{
    public TestHandler(QuestionService questionService, UserService userService, TestingContextService testingContextService) {
        super(questionService, userService, testingContextService);
    }


    @Override
    public List<SendMessage> handle(Update update) {
        List<SendMessage> messages = new ArrayList<>();

        Long tId = Long.valueOf(update.getMessage().getFrom().getId());

        User u = userService.findByTelegramId(tId);
        if (u == null) {
            messages.add(createSendMessage(update, "Зарегестрируйтесь для того чтобы пройти тест."));
            return messages;
        }

        testingContextService.deleteContext(tId);
        List<Question> context = questionService.findAll();
        testingContextService.putContext(tId, context);

        messages.add(createQuestionMessage(update, context.get(0)));
        return messages;
    }

    @Override
    public String getDescription() {
        return "Начинает тестирование";
    }
}
