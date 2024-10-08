package net.happykoo.toby.test.user;

import lombok.extern.slf4j.Slf4j;
import net.happykoo.toby.config.ApplicationConfig;
import net.happykoo.toby.constant.Level;
import net.happykoo.toby.dto.User;
import net.happykoo.toby.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static net.happykoo.toby.constant.Level.BRONZE;
import static net.happykoo.toby.service.user.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
import static net.happykoo.toby.service.user.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ApplicationConfig.class)
@Transactional
@Slf4j
public class UserServiceBootTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserService testUserService;

    @Autowired
    private PlatformTransactionManager transactionManager;
    private List<User> testUsers;

    @BeforeEach
    public void setup() {
        testUsers = List.of(
            new User("test1", "테스트1", BRONZE, MIN_LOGIN_COUNT_FOR_SILVER - 1, 0),
            new User("test2", "테스트2", BRONZE, MIN_LOGIN_COUNT_FOR_SILVER, 0),
            new User("test3", "테스트3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
            new User("test4", "테스트4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
            new User("test5", "테스트5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    @DisplayName("add 메서드 테스트 :: level이 null 이면 BRONZE로 셋팅, level이 null이 아니면 그대로")
    public void addTest() {
        User testUserWithoutLevel = testUsers.get(0);
        testUserWithoutLevel.setLevel(null);

        User testUserWithLevel = testUsers.get(2);

        userService.add(testUserWithoutLevel);
        userService.add(testUserWithLevel);

        assertEquals(BRONZE, userService.findById(testUserWithoutLevel.getId()).getLevel());
        assertEquals(testUserWithLevel.getLevel(), userService.findById(testUserWithLevel.getId()).getLevel());
    }

    @Test
    @DisplayName("upgradeLevels 메서드 테스트 :: 정상적인 경우")
    public void upgradeLevelsTest() {
        for(User testUser : testUsers) {
            userService.add(testUser);
        }
        userService.upgradeLevels();

        checkUpgradeLevel(testUsers.get(0), false);
        checkUpgradeLevel(testUsers.get(1), true);
        checkUpgradeLevel(testUsers.get(2), false);
        checkUpgradeLevel(testUsers.get(3), true);
        checkUpgradeLevel(testUsers.get(4), false);
    }

    @Test
    @DisplayName("upgradeLevels 메서드 테스트 :: 예외가 발생한 경우 롤백")
    public void upgradeLevelsRollbackTest() {
        for(User testUser : testUsers) {
            userService.add(testUser);
        }

        assertThrows(DataAccessResourceFailureException.class, () -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
            transactionTemplate.execute(status -> {
                testUserService.upgradeLevels();
                return null;
            });
        });

        checkUpgradeLevel(testUsers.get(0), false);
        checkUpgradeLevel(testUsers.get(1), false);
        checkUpgradeLevel(testUsers.get(2), false);
        checkUpgradeLevel(testUsers.get(3), false);
        checkUpgradeLevel(testUsers.get(4), false);
    }

    @Test
    @DisplayName("트랜잭션 전파 테스트 :: 트랜잭션 속성 readOnly를 위반한 경우")
    @Transactional(readOnly = true)
    public void transactionPropagationTest() {
        assertThrows(TransientDataAccessResourceException.class, () -> {
            userService.deleteAll();
            userService.add(testUsers.get(0));
            userService.add(testUsers.get(1));
        });
    }

    private void checkUpgradeLevel(User testUser, boolean isUpgrade) {
        User reloadUser = userService.findById(testUser.getId());
        if (isUpgrade) {
            assertEquals(testUser.getLevel().getNextLevel(), reloadUser.getLevel());
        } else {
            assertEquals(testUser.getLevel(), reloadUser.getLevel());
        }
    }
}
