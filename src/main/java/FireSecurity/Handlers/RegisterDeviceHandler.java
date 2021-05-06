package FireSecurity.Handlers;

import FireSecurity.Models.Device;
import FireSecurity.Models.User;
import FireSecurity.Service.QuestionService;
import FireSecurity.Service.TestingContextService;
import FireSecurity.Service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("/regdevice")
public class RegisterDeviceHandler extends TextHandler{

    private static final String EXPECTED_DATE_FORMAT = "dd.MM.yyyy";

    public RegisterDeviceHandler(QuestionService questionService, UserService userService, TestingContextService testingContextService) {
        super(questionService, userService, testingContextService);
    }

    @Override
    public String getDescription() {
        return "Регистрирует новое УС, формат \"команда серийный_номер годен_до(формат dd.mm.yyyy)\"";
    }

    @Override
    public List<SendMessage> handle(Update update) {
        List<SendMessage> messages = new ArrayList<>();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        messages.add(sendMessage);

        String text = update.getMessage().getText();
        String[] s = text.trim().split(" ");

        String command = s[0];
        String serial = s[1];
        String expired = s[2];

        SimpleDateFormat format = new SimpleDateFormat(EXPECTED_DATE_FORMAT);
        Date date;
        try {
            date = format.parse(expired);
        } catch (ParseException e) {
            e.printStackTrace();
            sendMessage.setText("Ошибка парсинга даты, введено: " + expired + ", ожидаемый формат: " + EXPECTED_DATE_FORMAT);
            return messages;
        }

        Long tId = Long.valueOf(update.getMessage().getFrom().getId());
        User u = userService.findByTelegramId(tId);
        if (u == null) {
            sendMessage.setText("Вы не зарегестрированы");
            return messages;
        }
        Device d = new Device();
        d.setDeviceSerial(serial);
        d.setExpirationDate(date);
        d.setUser(u);
        u.getDevices().add(d);

        userService.save(u);

        sendMessage.setText("Новое УС для пользователя: " + u.getTelegramId() + " зарегестрировано");
        return messages;
    }
}
