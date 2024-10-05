package net.happykoo.toby.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class SqlServiceImportSelector implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        String resourcePath = (String) importingClassMetadata
                .getAnnotationAttributes(EnableSqlService.class.getName())
                .get("value");

        // SqlServiceConfig의 빈을 등록하면서 경로 값을 생성자 인자로 전달
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SqlServiceConfig.class);
        builder.addConstructorArgValue(resourcePath);  // 생성자 인자로 경로 값 전달

        // 빈 등록
        registry.registerBeanDefinition("sqlServiceConfig", builder.getBeanDefinition());

    }
}
