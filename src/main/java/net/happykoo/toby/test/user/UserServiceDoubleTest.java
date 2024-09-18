package net.happykoo.toby.test.user;

import net.happykoo.toby.constant.Level;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dao.UserDaoJdbc;
import net.happykoo.toby.dto.User;
import net.happykoo.toby.service.sql.SimpleSqlService;
import net.happykoo.toby.service.sql.SqlService;
import net.happykoo.toby.service.user.UserService;
import net.happykoo.toby.service.user.UserServiceImpl;
import net.happykoo.toby.test.test_double.UserDaoFake;
import net.happykoo.toby.test.test_double.UserDaoStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceDoubleTest {

    @Test
    @DisplayName("add 테스트 :: Stub 객체 이용")
    public void addStubTest() {
        final String testId = "happykoo";

        UserDao userDaoStub = new UserDaoStub();
        UserService userService = new UserServiceImpl(userDaoStub);

        User user = User.builder()
                .id(testId)
                .build();

        userService.add(user);

        assertEquals(user.getId(), userDaoStub.findById(testId).getId());
        assertEquals(Level.BRONZE, userDaoStub.findById(testId).getLevel());
    }

    @Test
    @DisplayName("add 테스트 :: Fake 객체 이용")
    public void addFakeTest() {
        final String testId = "happykoo";

        UserDao userDaoFake = new UserDaoFake();
        UserService userService = new UserServiceImpl(userDaoFake);

        User user = User.builder()
                .id(testId)
                .build();

        userService.add(user);

        assertEquals(user.getId(), userDaoFake.findById(testId).getId());
        assertEquals(Level.BRONZE, userDaoFake.findById(testId).getLevel());
    }

    @Test
    @DisplayName("add 테스트 :: Mock 객체 이용")
    public void addMockTest() {
        final String testId = "happykoo";

        UserDao userDaoMock = mock(UserDao.class);
        UserService userService = new UserServiceImpl(userDaoMock);

        User user = User.builder()
                .id(testId)
                .build();

        userService.add(user);

        verify(userDaoMock).add(user);
    }

    @Test
    @DisplayName("add 테스트 :: Spy 객체 이용")
    public void addSpyTest() {
        final String testId = "happykoo";

        //실제 객체 생성
        UserDao userDao = new UserDaoJdbc(getDataSource(), getSqlService());
        //Spy 객체
        UserDao userDaoSpy = spy(userDao);

        //add 메서드 호출 시 아무것도 동작 안하게 mocking
        doNothing().when(userDaoSpy).add(any(User.class));

        UserService userService = new UserServiceImpl(userDaoSpy);

        User user = User.builder()
                .id(testId)
                .build();

        userService.add(user);
        verify(userDaoSpy).add(user);
    }

    private DataSource getDataSource() {
        return new SimpleDriverDataSource();
    }

    private SqlService getSqlService() {
        return new SimpleSqlService(getSqlMap());
    }

    private Map<String, String> getSqlMap() {
        return new HashMap<>();
    }
}
