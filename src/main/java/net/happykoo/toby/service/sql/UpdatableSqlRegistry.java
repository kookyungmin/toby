package net.happykoo.toby.service.sql;

import net.happykoo.toby.dto.SqlInfo;

import java.util.List;

public interface UpdatableSqlRegistry extends SqlRegistry {
    void update(SqlInfo sqlInfo);
    void update(List<SqlInfo> sqlInfoList);
}
