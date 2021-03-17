package FireSecurity.Handlers;

import FireSecurity.Service.QuestionService;
import FireSecurity.Service.TestingContextService;
import FireSecurity.Service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component("/delete")
/**
 * удаление пользователя
 */
public class DeleteHandler extends TextHandler {

    public DeleteHandler(QuestionService questionService, UserService userService, TestingContextService testingContextService) {
        super(questionService, userService, testingContextService);
    }

    @Override
    public List<SendMessage> handle(Update update) {
        List<SendMessage> messages = new ArrayList<>();

        userService.deleteUserByTelegramId(Long.valueOf(update.getMessage().getFrom().getId()));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText("Пользователь с ID: " + update.getMessage().getFrom().getId() + " удален");
        messages.add(sendMessage);
        return messages;
    }

    @Override
    public String getDescription() {
        return "Удаляет текущего пользователя и все его данные";
    }
}