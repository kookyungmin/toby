package net.happykoo.toby.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@Configuration
@EnableSqlService(value = "classpath:sql/sql-mapper.xml")
@Import({ DataSourceConfig.class, AopConfig.class })
@ComponentScan(basePackages = "net.happykoo.toby",
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "net.happykoo.toby.config.*"))
public class ApplicationConfig {}
