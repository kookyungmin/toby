package net.happykoo.toby.test;

import net.happykoo.toby.config.ApplicationConfig;
import net.happykoo.toby.constant.Level;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;
import net.happykoo.toby.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static net.happykoo.toby.service.UserService.MIN_LOGIN_COUNT_FOR_SILVER;
import static net.happykoo.toby.service.UserService.MIN_RECOMMEND_FOR_GOLD;

@SpringBootTest(classes = ApplicationConfig.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private List<User> testUsers;

    @BeforeEach
    public void setup() {
        userDao.deleteAll();
        testUsers = List.of(
            new User("test1", "테스트1", Level.BRONZE, MIN_LOGIN_COUNT_FOR_SILVER - 1, 0),
            new User("test2", "테스트2", Level.BRONZE, MIN_LOGIN_COUNT_FOR_SILVER, 0),
            new User("test3", "테스트3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
            new User("test4", "테스트4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
            new User("test5", "테스트5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

}
