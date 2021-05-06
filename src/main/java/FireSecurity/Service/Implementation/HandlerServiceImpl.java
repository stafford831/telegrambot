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
public class HandlerServiceImpl implements HandlerService {

    private final Map<String, BaseHandler> handlers;


    @Getter
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

    @Override
    public UpdateHandler getHandler(String key) {
        return handlers.get(key);
    }

    private String extractHandlerNameFromText(String s) {
        return s.split(" ")[0];
    }

    private String extractHandlerNameFromCallback(String s) throws ParseException {
        JSONObject data = (JSONObject) new JSONParser().parse(s);
        return (String)data.get("handlerName");
    }

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
