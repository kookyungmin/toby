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
            //객체 직접 생성
            DaoFactory daoFactory = new DaoFactory();
            UserDao userDao1 = daoFactory.userDao();
            UserDao userDao2 = daoFactory.userDao();

            System.out.println(userDao1.equals(userDao2));

            //ApplicationContext 사용
            ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
            UserDao userDao3 = context.getBean("userDao", UserDao.class);
            UserDao userDao4 = context.getBean("userDao", UserDao.class);

            System.out.println(userDao3.equals(userDao4));





//            // id 가 1인 유저 조회
//            User user = userDao.findById(1);
//            String nickName = Optional.ofNullable(user)
//                    .map(User::getNickName)
//                    .orElse(null);
//            System.out.println("user nickName >> " + nickName);
//
//            // 전체 유저 조회
//            int totalCount = userDao.findAll().size();
//            System.out.println("user all count >> " + totalCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
