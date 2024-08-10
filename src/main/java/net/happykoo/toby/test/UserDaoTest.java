package net.happykoo.toby.test;

import net.happykoo.toby.config.DaoFactory;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = DaoFactory.class)
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        System.out.println("UserDao >> " + userDao);
    }

    @Test
    @DisplayName("findById() 메서드 테스트 :: 해당 유저가 없을 때, EmptyResultDataAccessException 발생")
    public void findByIdExceptionTest() {
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findById("UNKNOWN_ID"));
    }

    @Test
    @DisplayName("add(), findById(), deleteById() 메서드 테스트 :: 정상적인 경우")
    public void addAndFindByIdAndDeleteByIdTest() {
        User newUser = User.builder()
                .id("happykoo")
                .nickName("해피쿠")
                .build();

        userDao.add(newUser);
        User user = userDao.findById(newUser.getId());

        //해피쿠 유저 데이터가 제대로 삽입/조회 되었는지 테스트
        String expectedNickName = newUser.getNickName();
        String actualNickName = Optional.ofNullable(user)
                .map(User::getNickName)
                .orElse(null);

        assertEquals(expectedNickName, actualNickName);

        userDao.deleteById(newUser.getId());

        //해피쿠 유저 데이터가 제대로 삭제되었는지 테스트
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findById(newUser.getId()));
    }

    @Test
    @DisplayName("add() 메서드 테스트 :: ID가 중복된 경우")
    public void addExceptionTest() {
        User newUser = User.builder()
                .id("happykoo")
                .nickName("해피쿠")
                .build();

        userDao.add(newUser);
        //중복된 ID로 삽입시 DataAccessException 발생
        assertThrows(DataAccessException.class, () -> userDao.add(newUser));

        //중복된 ID로 삽입시 DataAccessException 하위 클래스 DuplicateKeyException 발생
        assertThrows(DuplicateKeyException.class, () -> userDao.add(newUser));

        userDao.deleteById(newUser.getId());
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findById(newUser.getId()));
    }
}
