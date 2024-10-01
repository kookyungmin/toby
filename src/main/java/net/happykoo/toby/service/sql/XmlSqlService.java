package net.happykoo.toby.service.sql;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import net.happykoo.toby.dto.SqlMap;
import net.happykoo.toby.dto.SqlType;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class XmlSqlService implements SqlService {
    private Map<String, String> sqlMap = new HashMap<>();
    private String filePath;

    public XmlSqlService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return Optional.ofNullable(sqlMap.get(key))
                .orElseThrow(() -> new SqlRetrievalFailureException(key + " is not found in SQL map"));
    }

    @PostConstruct
    public void onload() {
        try {
            String contextPath = SqlMap.class.getPackage().getName();
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream inputStream = new ClassPathResource(filePath).getInputStream();
            SqlMap map = (SqlMap) unmarshaller.unmarshal(inputStream);

            for (SqlType type : map.getSql()) {
                sqlMap.put(type.getKey(), type.getValue());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
