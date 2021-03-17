package FireSecurity.Handlers;

import FireSecurity.Service.QuestionService;
import FireSecurity.Service.TestingContextService;
import FireSecurity.Service.UserService;

/**
 * предок для всех handlerов, обрабатывающих колбеки
 */
public class CallbackHandler extends BaseHandler{
    public CallbackHandler(QuestionService questionService, UserService userService, TestingContextService testingContextService) {
        super(questionService, userService, testingContextService);
    }

    @Override
    public HandlerType getType() {
        return HandlerType.CALLBACK_HANDLER;
    }
}
