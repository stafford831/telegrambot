package FireSecurity.Service.Implementation;

import FireSecurity.InterLayer.UserDao;
import FireSecurity.Models.User;
import FireSecurity.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
/**
 *
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User findByTelegramId(Long tId) {
        return userDao.findByTelegramId(tId);
    }

    @Override
    public User save(User u) {
        return userDao.save(u);
    }

    @Override
    public void delete(User u) {
        userDao.delete(u);
    }

    @Override
    public void deleteUserByTelegramId(Long telegramId) {
        userDao.deleteUserByTelegramId(telegramId);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

}
