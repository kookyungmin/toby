package net.happykoo.toby.service.sql;

import net.happykoo.toby.dto.SqlInfo;

import java.util.List;

public interface SqlRegistry {
    void register(SqlInfo sqlInfo);
    String findById(String id) throws SqlRetrievalFailureException;
}
