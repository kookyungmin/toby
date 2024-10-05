package net.happykoo.toby.service.sql;

import net.happykoo.toby.dto.SqlInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {
    private Map<String, String> sqlMap = new ConcurrentHashMap<>();

    @Override
    public void register(SqlInfo sqlInfo) {
        sqlMap.put(sqlInfo.getId(), sqlInfo.getSql());
    }

    @Override
    public String findById(String id) throws SqlRetrievalFailureException {
        return Optional.ofNullable(sqlMap.get(id))
                .orElseThrow(() -> new SqlRetrievalFailureException(id + " is not found in SQL map"));
    }

    @Override
    public void update(SqlInfo sqlInfo) {
        String id = sqlInfo.getId();
        if (!sqlMap.containsKey(id)) {
            throw new SqlRetrievalFailureException(id + " is not found in SQL map");
        }
        sqlMap.put(id, sqlInfo.getSql());
    }

    @Override
    public void update(List<SqlInfo> sqlInfoList) {
        for(SqlInfo sqlInfo : sqlInfoList) {
            update(sqlInfo);
        }
    }
}
