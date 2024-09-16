package net.happykoo.toby.config;

import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dao.UserDaoJdbc;
import net.happykoo.toby.service.TestUserService;
import net.happykoo.toby.service.UserService;
import net.happykoo.toby.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@Import({ DataSourceConfig.class, AopConfig.class })
@ImportResource("classpath:/sql/sql-mapper.xml")
public class ApplicationConfig {
    @Resource(name = "sqlMap")
    private Map<String, String> sqlMap;

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
        return new UserDaoJdbc(dataSource, sqlMap);
    }
}
