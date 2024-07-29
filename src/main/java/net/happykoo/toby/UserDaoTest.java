package net.happykoo.toby;

import net.happykoo.toby.dao.DaoFactory;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class UserDaoTest {
    @Test
    @DisplayName("findById() 메서드 테스트 :: 해당 유저가 없을 때, EmptyResultDataAccessException 발생")
    public void findByIdExceptionTest() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);

        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findById("UNKNOWN_ID"));
    }

    @Test
    public void addAndFindByIdAndDeleteByIdTest() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);

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
        assertNull(userDao.findById(newUser.getId()));
    }
}
