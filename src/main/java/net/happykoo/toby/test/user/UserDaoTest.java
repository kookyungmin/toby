package net.happykoo.toby.test.user;

import net.happykoo.toby.config.ApplicationConfig;
import net.happykoo.toby.constant.Level;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ApplicationConfig.class)
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    private List<User> testUsers;

    @BeforeEach
    public void setUp() {
        testUsers = List.of(
            new User("happykoo", "해피쿠", Level.BRONZE, 49, 0),
            new User("marco", "마르코", Level.BRONZE, 50, 0),
            new User("bean", "콩", Level.SILVER, 60, 29)
        );
        userDao.deleteAll();
    }

    @Test
    @DisplayName("findAll(), deleteAll() 메서드 테스트 :: 정상적인 경우")
    public void findAllAndDeleteAllTest() {
        //처음 전체 유저 수 0건
        assertEquals(0, userDao.findAll().size());

        for(User testUser : testUsers) {
            userDao.add(testUser);
        }

        //전체 유저 수 testUsers.size() 건
        assertEquals(testUsers.size(), userDao.findAll().size());

        //전체 삭제 후 전체 유저 수 0건
        userDao.deleteAll();
        assertEquals(0, userDao.findAll().size());
    }

    @Test
    @DisplayName("update() 메서드 테스트 :: 레벨 수정")
    public void updateTest() {
        User testUser = testUsers.get(0);

        userDao.add(testUser);
        assertEquals(Level.BRONZE, userDao.findById(testUser.getId()).getLevel());

        //레벨 변경
        testUser.setLevel(Level.GOLD);
        userDao.update(testUser);

        assertEquals(Level.GOLD, userDao.findById(testUser.getId()).getLevel());
    }

    @Test
    @DisplayName("findById() 메서드 테스트 :: 해당 유저가 없을 때, EmptyResultDataAccessException 발생")
    public void findByIdExceptionTest() {
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findById("UNKNOWN_ID"));
    }

    @Test
    @DisplayName("add(), findById(), deleteById() 메서드 테스트 :: 정상적인 경우")
    public void addAndFindByIdAndDeleteByIdTest() {
        User testUser = testUsers.get(0);

        userDao.add(testUser);
        User user = userDao.findById(testUser.getId());

        //해피쿠 유저 데이터가 제대로 삽입/조회 되었는지 테스트
        assertUserEquals(testUser, user);

        userDao.deleteById(testUser.getId());

        //해피쿠 유저 데이터가 제대로 삭제되었는지 테스트
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findById(testUser.getId()));
    }

    @Test
    @DisplayName("add() 메서드 테스트 :: ID가 중복된 경우")
    public void addExceptionTest() {
        User testUser = testUsers.get(0);

        userDao.add(testUser);
        //중복된 ID로 삽입시 DataAccessException 발생
        assertThrows(DataAccessException.class, () -> userDao.add(testUser));

        //중복된 ID로 삽입시 DataAccessException 하위 클래스 DuplicateKeyException 발생
        assertThrows(DuplicateKeyException.class, () -> userDao.add(testUser));
    }

    private void assertUserEquals(User source, User target) {
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getNickName(), target.getNickName());
        assertEquals(source.getLevel(), target.getLevel());
        assertEquals(source.getLoginCount(), target.getLoginCount());
        assertEquals(source.getRecommendCount(), target.getRecommendCount());
    }
}
