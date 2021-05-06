package FireSecurity.Handlers;

import FireSecurity.Models.User;
import FireSecurity.Service.QuestionService;
import FireSecurity.Service.TestingContextService;
import FireSecurity.Service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component("/register")
public class RegisterHandler extends TextHandler{


    public RegisterHandler(QuestionService questionService, UserService userService, TestingContextService testingContextService) {
        super(questionService, userService, testingContextService);
    }

    @Override
    public String getDescription() {
        return "Регистрирует нового пользователя";
    }

    @Override
    public List<SendMessage> handle(Update update) {
        List<SendMessage> messages = new ArrayList<>();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        messages.add(sendMessage);

        Long tId = Long.valueOf(update.getMessage().getFrom().getId());

        User u = userService.findByTelegramId(tId);
        if (u != null) {
            sendMessage.setText("Пользователь с таким ID: " + u.getTelegramId() + ", уже существует");
            return messages;
        }

        u = new User();
        u.setTelegramId(tId);
        u = userService.save(u);

        sendMessage.setText("Новый пользователь с ID: " + u.getTelegramId() + " добавлен");
        return messages;
    }
}
