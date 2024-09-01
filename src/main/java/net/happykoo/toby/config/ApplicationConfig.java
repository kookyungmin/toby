package net.happykoo.toby.config;

import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dao.UserDaoJdbc;
import net.happykoo.toby.service.TestUserService;
import net.happykoo.toby.service.UserService;
import net.happykoo.toby.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@Import({ DataSourceConfig.class, AopConfig.class })
@EnableAspectJAutoProxy
public class ApplicationConfig {
    @Bean
    public UserService userService(UserDao userDao) {
        return new UserServiceImpl(userDao);
    }

    @Bean
    public UserService testUserService(UserDao userDao) {
        return new TestUserService(userDao);
    }


    @Bean
    public UserDao userDao(DataSource dataSource) {
        return new UserDaoJdbc(dataSource);
    }
}
