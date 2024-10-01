package net.happykoo.toby.config;

import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dao.UserDaoJdbc;
import net.happykoo.toby.service.sql.SimpleSqlService;
import net.happykoo.toby.service.sql.SqlService;
import net.happykoo.toby.service.sql.XmlSqlService;
import net.happykoo.toby.service.user.TestUserService;
import net.happykoo.toby.service.user.UserService;
import net.happykoo.toby.service.user.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@Import({ DataSourceConfig.class, AopConfig.class })
public class ApplicationConfig {

    @Bean
    public SqlService sqlService() {
        return new XmlSqlService("/sql/sql-mapper.xml");
    }

    @Bean
    public UserDao userDao(DataSource dataSource,
                           SqlService sqlService) {
        return new UserDaoJdbc(dataSource, sqlService);
    }

    @Bean
    public UserService userService(UserDao userDao) {
        return new UserServiceImpl(userDao);
    }

    @Bean
    public UserService testUserService(UserDao userDao) {
        return new TestUserService(userDao);
    }

}
