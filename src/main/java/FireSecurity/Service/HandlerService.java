package FireSecurity.Service;

import FireSecurity.Handlers.UpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface HandlerService {

    UpdateHandler getHandler(Update update);

    UpdateHandler getHandler(String key);

    UpdateHandler getDefaultHandler();

}
