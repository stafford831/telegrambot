package FireSecurity.Service;

import FireSecurity.Models.User;

import java.util.List;

/**
 * сервис для работы с пользователем
 */
public interface UserService {

    User findByTelegramId(Long tId);

    User save(User u);

    void delete(User u);

    void deleteUserByTelegramId(Long telegramId);

    List<User> findAll();
}