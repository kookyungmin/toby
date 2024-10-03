package net.happykoo.toby.service.sql;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import net.happykoo.toby.dto.SqlInfo;
import net.happykoo.toby.dto.SqlMap;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class JaxbSqlLoader implements SqlLoader {
    private String filePath;

    public JaxbSqlLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<SqlInfo> load() {
        try {
            String contextPath = SqlMap.class.getPackage().getName();
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream inputStream = new ClassPathResource(filePath).getInputStream();
            SqlMap map = (SqlMap) unmarshaller.unmarshal(inputStream);

            return map.getSql().stream()
                    .map(s -> SqlInfo.builder()
                            .id(s.getKey())
                            .sql(s.getValue())
                            .build())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
