package net.happykoo.toby.config;

import net.happykoo.toby.advisor.TxAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

//@Configuration
public class AopAdvisorConfig {
    @Bean
    public AspectJExpressionPointcut transactionPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        pointcut.setExpression("execution(* *..*ServiceImpl.upgrade*(..))");

        return pointcut;
    }

    @Bean
    public TxAdvice transactionAdvice(PlatformTransactionManager transactionManager) {
        return new TxAdvice(transactionManager);
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(TxAdvice transactionAdvice,
                                                     AspectJExpressionPointcut transactionPointcut) {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(transactionPointcut);
        advisor.setAdvice(transactionAdvice);

        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
