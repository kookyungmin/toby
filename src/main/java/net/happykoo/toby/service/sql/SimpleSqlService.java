package net.happykoo.toby.service.sql;

import java.util.Map;
import java.util.Objects;

public class SimpleSqlService implements SqlService {
    private Map<String, String> sqlMap;

    public SimpleSqlService(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);

        if (Objects.isNull(sql)) {
            throw new SqlRetrievalFailureException("SQL for key is not found");
        }

        return sql;
    }
}
