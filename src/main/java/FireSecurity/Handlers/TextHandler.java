package FireSecurity.Handlers;

import FireSecurity.Service.QuestionService;
import FireSecurity.Service.TestingContextService;
import FireSecurity.Service.UserService;

public abstract class TextHandler extends BaseHandler{

    public TextHandler(QuestionService questionService, UserService userService, TestingContextService testingContextService) {
        super(questionService, userService, testingContextService);
    }

    public abstract String getDescription();

    @Override
    public HandlerType getType() {
        return HandlerType.TEXT_HANDLER;
    }
}

