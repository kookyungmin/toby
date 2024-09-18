package net.happykoo.toby.test.user;

import net.happykoo.toby.constant.Level;
import net.happykoo.toby.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Propagation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
    }

    @Test
    @DisplayName("upgradeLevel 메서드 테스트 :: 정상적인 경우")
    public void upgradeLevelTest() {
        for(Level level : Level.values()) {
            if (Objects.isNull(level.getNextLevel())) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertEquals(level.getNextLevel(), user.getLevel());
        }
    }

    @Test
    @DisplayName("upgradeLevel 메서드 테스트 :: 에외 발생")
    public void upgradeLevelThrowTest() {
        for(Level level : Level.values()) {
            if (Objects.nonNull(level.getNextLevel())) continue;
            user.setLevel(level);
            assertThrows(IllegalStateException.class, () -> user.upgradeLevel());
        }
    }
}


