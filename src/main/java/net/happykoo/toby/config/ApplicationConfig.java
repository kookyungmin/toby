package net.happykoo.toby.config;

import net.happykoo.toby.advisor.TxAdvice;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dao.UserDaoJdbc;
import net.happykoo.toby.service.TestUserService;
import net.happykoo.toby.service.UserService;
import net.happykoo.toby.service.UserServiceImpl;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
@Import({ DataSourceConfig.class, AopConfig.class })
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
