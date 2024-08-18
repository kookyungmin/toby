package net.happykoo.toby.test;

import net.happykoo.toby.constant.Level;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;
import net.happykoo.toby.service.UserService;
import net.happykoo.toby.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static net.happykoo.toby.constant.Level.*;
import static net.happykoo.toby.service.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
import static net.happykoo.toby.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private List<User> testUsers;
    private UserService userService;
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        this.userDao = mock(UserDao.class);
        this.userService = new UserServiceImpl(userDao);
        this.testUsers = List.of(
            new User("test1", "테스트1", BRONZE, MIN_LOGIN_COUNT_FOR_SILVER - 1, 0),
            new User("test2", "테스트2", BRONZE, MIN_LOGIN_COUNT_FOR_SILVER, 0),
            new User("test3", "테스트3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
            new User("test4", "테스트4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
            new User("test5", "테스트5", GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    @DisplayName("add() 테스트 :: level 이 null 인 경우")
    public void addTest() {
        User testUser = testUsers.get(0);
        testUser.setLevel(null);

        //findById 메서드 mocking
        when(userDao.findById(testUser.getId()))
            .thenReturn(testUser);

        userService.add(testUser);

        assertEquals(testUser.getId(), userDao.findById(testUser.getId()).getId());
        assertEquals(Level.BRONZE, userDao.findById(testUser.getId()).getLevel());
    }

    @Test
    @DisplayName("upgradeLevels() 테스트")
    public void upgradeLevelsTest() {
        //findAll 메서드 mocking
        when(userDao.findAll())
            .thenReturn(this.testUsers);

        userService.upgradeLevels();

        //findAll 메서드 1번 호출되었는지 테스트
        verify(userDao, times(1)).findAll();

        ArgumentCaptor<User> userArgCaptor = ArgumentCaptor.forClass(User.class);
        //update 메서드 2번 호출되었는지 테스트(test2, test4)
        verify(userDao, times(2)).update(userArgCaptor.capture());

        //update 메서드 호출된 파라미터가 test2, test4 유저인지 테스트
        List<User> userArgs = userArgCaptor.getAllValues();
        assertEquals(testUsers.get(1), userArgs.get(0));
        assertEquals(testUsers.get(3), userArgs.get(1));

        //test2, test4 에 대해서는 update 메서드 호출되고, test1, test3, test5 에 대해서는 update 메서드 호출 안된 것 검증
        verify(userDao, never()).update(testUsers.get(0));
        verify(userDao).update(testUsers.get(1));
        verify(userDao, never()).update(testUsers.get(2));
        verify(userDao).update(testUsers.get(3));
        verify(userDao, never()).update(testUsers.get(4));

        //test2, test4 레벨 제대로 업그레이드 되었는지 검증
        assertEquals(SILVER, testUsers.get(1).getLevel());
        assertEquals(GOLD, testUsers.get(3).getLevel());
    }
}
