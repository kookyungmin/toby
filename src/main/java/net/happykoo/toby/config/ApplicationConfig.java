package net.happykoo.toby.config;

import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dao.UserDaoJdbc;
import net.happykoo.toby.factory.TxProxyFactoryBean;
import net.happykoo.toby.handler.TxAdvice;
import net.happykoo.toby.handler.TxInvocationHandler;
import net.happykoo.toby.service.UserService;
import net.happykoo.toby.service.UserServiceImpl;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
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
    public NameMatchMethodPointcut transactionPointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("upgrade*");

        return pointcut;
    }

    @Bean
    public TxAdvice transactionAdvice() {
        return new TxAdvice(transactionManager());
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor() {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(transactionPointcut());
        advisor.setAdvice(transactionAdvice());

        return advisor;
    }

    @Bean
    public ProxyFactoryBean userService() {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();

        factoryBean.setTarget(new UserServiceImpl(userDao()));
        factoryBean.setInterceptorNames("transactionAdvisor");

        return factoryBean;
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
