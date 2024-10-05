package net.happykoo.toby.service.sql;

import org.springframework.core.io.ClassPathResource;

public class XmlSqlService extends BaseSqlService {
    public XmlSqlService(String filePath) {
        super(new JaxbSqlLoader(new ClassPathResource(filePath)), new ConcurrentHashMapSqlRegistry());
    }
}
