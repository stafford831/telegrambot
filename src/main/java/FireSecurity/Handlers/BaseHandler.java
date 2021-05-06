package FireSecurity.Handlers;

import FireSecurity.Models.Option;
import FireSecurity.Models.Question;
import FireSecurity.Service.QuestionService;
import FireSecurity.Service.TestingContextService;
import FireSecurity.Service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor

public abstract class BaseHandler implements UpdateHandler{

    protected final QuestionService questionService;
    protected final UserService userService;
    protected final TestingContextService testingContextService;

    @Override
    public List<SendMessage> handle(Update update) {
        return new ArrayList<>();
    }

    public abstract HandlerType getType();

    protected SendMessage createQuestionMessage(Update update, Question question) {
        SendMessage sendMessage = new SendMessage();

        String chatId = String.valueOf(update.hasMessage()
                ? update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId());

        sendMessage.setChatId(chatId);
        sendMessage.setText(question.getText());

        InlineKeyboardMarkup inlineKeyboardMarkup =new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();

        for ( Option option: question.getOptions()) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(option.getText());

            JSONObject data = new JSONObject();

            data.put("handlerName", "test_answer");
            data.put("question_id", String.valueOf(question.getId()));
            data.put("option_id", String.valueOf(option.getId()));

            inlineKeyboardButton.setCallbackData(data.toJSONString());

            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    protected SendMessage createSendMessage(Update update, String text) {
        SendMessage sendMessage = new SendMessage();
        String chatId = String.valueOf(update.hasMessage()
                ? update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        return sendMessage;
    }
}
