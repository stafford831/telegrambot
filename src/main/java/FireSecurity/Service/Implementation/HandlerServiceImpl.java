package FireSecurity.Service.Implementation;

import FireSecurity.Handlers.BaseHandler;
import FireSecurity.Handlers.HandlerType;
import FireSecurity.Handlers.TextHandler;
import FireSecurity.Handlers.UpdateHandler;
import FireSecurity.Service.HandlerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
/**
 * реализация handlerService
 *
 */
public class HandlerServiceImpl implements HandlerService {

    private final Map<String, BaseHandler> handlers;


    @Getter
    /**
     * формирование сообщение справки по handlerом в мапе
     */
    private final UpdateHandler defaultHandler = new UpdateHandler() {
        @Override
        public List<SendMessage> handle(Update update) {
            List<SendMessage> messages = new ArrayList<>();
            SendMessage sendMessage = new SendMessage();
            String chatId = String.valueOf(update.hasMessage()
                    ? update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId());
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText(getHandlersDescription());
            messages.add(sendMessage);
            return messages;
        }
    };

    /**
     * достаем тип обработчика из текста или колбека и возвращаем обработчик по этому ключу из мапы
     * @param update
     * @return обработчик по этому ключу из мапы
     */
    @Override
    public UpdateHandler getHandler(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return handlers.get(extractHandlerNameFromText(update.getMessage().getText()));
        } else if (update.hasCallbackQuery()){
            try {
                return handlers.get(extractHandlerNameFromCallback(update.getCallbackQuery().getData()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * @param key принимает ключ под которым handler сохранен в мапе,
     *            ключ это имя объекта, которое передается внутри аннотации component
     * @return обработчик сообщения
     */
    @Override
    public UpdateHandler getHandler(String key) {
        return handlers.get(key);
    }

    /**
     * обрабатываем строку введенную пользователем с целью получения ключа в мапе
     * @param s строка введенная пользователем
     * @return слово до первого пробела, которое является командой
     */
    private String extractHandlerNameFromText(String s) {
        return s.split(" ")[0];
    }

    /**
     * получаем имя обработчика/ключ к мапе из содержимого колбека,
     * содержимое это джейсон, в одном из полей ключ
     * @param s колбек
     * @return имя компонента/обработчика
     * @throws ParseException
     */
    private String extractHandlerNameFromCallback(String s) throws ParseException {
        JSONObject data = (JSONObject) new JSONParser().parse(s);
        return (String)data.get("handlerName");
    }

    /**
     * формируем строку со справкой, склеивая описание из обработчиков сообщения пользователя
     * @return строка справки
     */
    private String getHandlersDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append("Список доступных команд: \n");
        handlers.entrySet().stream()
                .filter(e -> e.getValue().getType().equals(HandlerType.TEXT_HANDLER))
                .forEach(e ->
                        builder
                                .append(e.getKey()).append(": ")
                                .append(((TextHandler)e.getValue()).getDescription())
                                .append("\n"));

        return builder.toString();
    }
}
