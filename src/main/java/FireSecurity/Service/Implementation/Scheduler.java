package FireSecurity.Service.Implementation;

import FireSecurity.Bot.Bot;
import FireSecurity.Models.Device;
import FireSecurity.Models.TestResult;
import FireSecurity.Models.User;
import FireSecurity.Service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@PropertySource("classpath:scheduler.properties")

public class Scheduler {

    private final Duration TESTING_INTERVAL;

    private final Bot bot;

    private final UserService userService;

    public Scheduler(@Value("${scheduler.testing.duration}") Duration testingInterval, Bot bot, UserService userService) {
        TESTING_INTERVAL = testingInterval;
        this.bot = bot;
        this.userService = userService;
    }

    @Scheduled(cron = "*/10 * * * * *")
    private void testResultExpiredNotificationTask() {
        System.out.println("testResultExpiredNotificationTask started...");
        List<User> users = userService.findAll();
        users.forEach(u -> {
            Optional<TestResult> r = u.getTestResults().stream().max(Comparator.comparing(TestResult::getCompletedOn));
            if (r.isPresent() && TESTING_INTERVAL.compareTo(Duration.between(r.get().getCompletedOn().toInstant(), Instant.now())) < 0) {
                bot.sendNotification(String.valueOf(u.getTelegramId()), "Вам необходимо пройти повторное тестирование");
            }
        });
    }

    @Scheduled(cron = "*/10 * * * * *")
    private void deviceExpiredNotificationTask() {
        System.out.println("deviceExpiredNotificationTask started...");
        List<User> users = userService.findAll();
        users.forEach(u -> {
            Optional<Device> r = u.getDevices().stream().max(Comparator.comparing(Device::getExpirationDate));
            if (r.isPresent() && r.get().getExpirationDate().compareTo(new Date()) < 0) {
                bot.sendNotification(String.valueOf(u.getTelegramId()), "Вышел срок годности вашего УС");
            }
        });
    }

}
