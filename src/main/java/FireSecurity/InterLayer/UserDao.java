package FireSecurity.InterLayer;

import FireSecurity.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * погуглить про DAO, DAO factory
 *
 */
public interface UserDao extends JpaRepository<User, Long> {

    User findByTelegramId(Long tId);

    void deleteUserByTelegramId(Long telegramId);
}
