package net.happykoo.toby.config;

import net.happykoo.toby.service.sql.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class SqlServiceConfig {
    private final String resourcePath;

    public SqlServiceConfig(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }

    @Bean
    public SqlLoader sqlLoader(ResourceLoader resourceLoader) {
        return new JaxbSqlLoader(resourceLoader.getResource(resourcePath));
    }

    @Bean
    public SqlService sqlService(SqlLoader sqlLoader,
                                 SqlRegistry sqlRegistry) {
        return new BaseSqlService(sqlLoader, sqlRegistry);
    }
}
