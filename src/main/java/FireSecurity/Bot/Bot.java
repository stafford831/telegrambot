package FireSecurity.Bot;

import FireSecurity.Handlers.UpdateHandler;
import FireSecurity.Service.HandlerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.hql.spi.id.MultiTableBulkIdStrategy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Getter
@Setter
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private final HandlerService srv;

    @Override
    public String getBotUsername() {
        return "someBotUserName";
    }

    @Override
    public String getBotToken() {
        return "someBotTokenName";
    }

    @Override
    public void onUpdateReceived(Update update) {

        System.out.println(update.getMessage());

        List<SendMessage> messages;

        UpdateHandler handler = srv.getHandler(update);
        if (handler == null) {
            handler = srv.getDefaultHandler();
        }

        messages = handler.handle(update);

        messages.forEach(m -> {
            try {
                execute(m);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });

    }

    private SendMessage createNotification(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }

    public void sendNotification(String chatId, String message) {
        try {
            execute(createNotification(chatId, message));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
