package net.happykoo.toby.service.sql;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
