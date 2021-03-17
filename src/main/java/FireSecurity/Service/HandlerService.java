package FireSecurity.Service;

import FireSecurity.Handlers.UpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Эта хуйня возвращает обработчик запроса,
 * и по хорошему она ближе к фабрике
 */
public interface HandlerService {

    UpdateHandler getHandler(Update update);

    UpdateHandler getHandler(String key);

    UpdateHandler getDefaultHandler();

}
