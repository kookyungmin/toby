package net.happykoo.toby.config;

import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dao.UserDaoJdbc;
import net.happykoo.toby.service.UserService;
import net.happykoo.toby.service.UserServiceImpl;
import net.happykoo.toby.service.UserServiceTx;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
public class ApplicationConfig {
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.jdbc-url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public UserService userService() {
        return new UserServiceTx(new UserServiceImpl(userDao()), transactionManager());
    }

    @Bean
    public UserDao userDao() {
        return new UserDaoJdbc(dataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        try {
            SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

            dataSource.setDriverClass((Class<Driver>) Class.forName(driverClassName));
            dataSource.setUrl(jdbcUrl);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);

            return dataSource;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
