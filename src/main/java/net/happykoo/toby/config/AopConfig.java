package net.happykoo.toby.config;

import net.happykoo.toby.advisor.TxAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class AopConfig {
    @Bean
    public TxAspect txAspect(PlatformTransactionManager transactionManager) {
        return new TxAspect(transactionManager);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
