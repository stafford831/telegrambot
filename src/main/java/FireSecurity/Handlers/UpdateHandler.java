package FireSecurity.Handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface UpdateHandler {
    /**
     *
     * @param update входное сообщение
     * @return список сообщений для возврата пользователю
     */
    List<SendMessage> handle(Update update);
}