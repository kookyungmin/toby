package net.happykoo.toby.service.sql;

import net.happykoo.toby.dto.SqlInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HashMapSqlRegistry implements SqlRegistry {
    private Map<String, String> sqlMap = new HashMap<>();

    @Override
    public void register(SqlInfo sqlInfo) {
        sqlMap.put(sqlInfo.getId(), sqlInfo.getSql());
    }

    @Override
    public String findById(String id) throws SqlRetrievalFailureException {
        return Optional.ofNullable(sqlMap.get(id))
                .orElseThrow(() -> new SqlRetrievalFailureException(id + " is not found in SQL map"));
    }
}
