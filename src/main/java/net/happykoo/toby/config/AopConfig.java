package net.happykoo.toby.config;

import net.happykoo.toby.advisor.TxAspect;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class AopConfig {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TransactionInterceptor transactionAdvice(PlatformTransactionManager transactionManager) {
        TransactionInterceptor txAdvice = new TransactionInterceptor();

        Properties txAttributes = new Properties();

        txAttributes.put("find*", "PROPAGATION_REQUIRED,readOnly");
        txAttributes.put("*", "PROPAGATION_REQUIRED");

        txAdvice.setTransactionManager(transactionManager);
        txAdvice.setTransactionAttributes(txAttributes);

        return txAdvice;
    }

    @Bean
    public Pointcut transactionPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("bean(*Service)");

        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(TransactionInterceptor txAdvice, Pointcut txPointcut) {
        return new DefaultPointcutAdvisor(txPointcut, txAdvice);
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }
}
