package net.happykoo.toby;

import net.happykoo.toby.dao.DaoFactory;
import net.happykoo.toby.dao.MysqlConnectionMaker;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class UserDaoTest {
    public static void main(String[] args) {
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
            UserDao userDao = context.getBean("userDao", UserDao.class);
            // id 가 1인 유저 조회
            User user = userDao.findById(1);
            String nickName = Optional.ofNullable(user)
                    .map(User::getNickName)
                    .orElse(null);
            System.out.println("user nickName >> " + nickName);

            // 전체 유저 조회
            int totalCount = userDao.findAll().size();
            System.out.println("user all count >> " + totalCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
