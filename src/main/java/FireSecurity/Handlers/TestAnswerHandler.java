package FireSecurity.Handlers;

import FireSecurity.Models.Option;
import FireSecurity.Models.Question;
import FireSecurity.Models.TestResult;
import FireSecurity.Models.User;
import FireSecurity.Service.QuestionService;
import FireSecurity.Service.TestingContextService;
import FireSecurity.Service.UserService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component("test_answer")
/**
 * класс анализирует ответы пользователя от нажатия кнопок при тестировании
 */
public class TestAnswerHandler extends CallbackHandler {
    public TestAnswerHandler(QuestionService questionService, UserService userService, TestingContextService testingContextService) {
        super(questionService, userService, testingContextService);
    }

    @Override
    public List<SendMessage> handle(Update update) {
        List<SendMessage> messages = new ArrayList<>();

        Long tId = Long.valueOf(update.getCallbackQuery().getFrom().getId());

        User u = userService.findByTelegramId(tId);
        if (u == null) {
            messages.add(createSendMessage(update, "Зарегестрируйтесь для того чтобы пройти тест."));
            return messages;
        }


        List<Question> context = testingContextService.getContext(tId);

        if (context == null) {
            messages.add(createSendMessage(update, "Контекст не найден, начните новый тест."));
            return messages;
        }

        if (!update.hasCallbackQuery()) {
            messages.add(createSendMessage(update, "Ошибка данных запроса."));
            return messages;
        }

        JSONObject data = null;
        try {
            data = (JSONObject) new JSONParser().parse(update.getCallbackQuery().getData());
        } catch (ParseException e) {
            e.printStackTrace();
            messages.add(createSendMessage(update, "Ошибка данных запроса."));
            return messages;
        }

        String qId = (String) data.get("question_id");
        String oId = (String) data.get("option_id");

        Question question = context.stream()
                .filter(q -> (q.getId().equals(Long.valueOf(qId)))).findAny().orElse(null);

        if (question == null) {
            messages.add(createSendMessage(update, "Ошибка, вопрос не найден."));
            return messages;
        }
        if (question.hasChosenOption()) {
            messages.add(createSendMessage(update, "Вы уже ответили на этот вопрос."));
            return messages;
        }

        Option option = question.getOptions().stream()
                .filter(o -> o.getId().equals(Long.valueOf(oId))).findAny().orElse(null);

        if (option == null) {
            messages.add(createSendMessage(update, "Ошибка, ответ не найден."));
            return messages;
        }

        question.setChosenOptionId(Long.valueOf(oId));

        Question nextQuestion = context.stream().filter(q -> !q.hasChosenOption()).findAny().orElse(null);
        if (nextQuestion != null) {
            messages.add(createQuestionMessage(update, nextQuestion));
            return messages;
        }

        List<Question> correct = context.stream().filter(Question::isChosenOptionCorrect).collect(Collectors.toList());
        List<Question> wrong = context.stream().filter(q -> !q.isChosenOptionCorrect()).collect(Collectors.toList());

        TestResult testResult = new TestResult();
        testResult.setCompletedOn(new Date());
        testResult.setUser(u);
        testResult.setPassed(wrong.isEmpty());

        u.getTestResults().add(testResult);

        userService.save(u);
        testingContextService.deleteContext(tId);

        messages.add(createSendMessage(update,
                (wrong.isEmpty() ? "Тест пройден" : "Тест не пройден") + ", результат: " + correct.size() + "/" + context.size()));
        return messages;
    }
}

