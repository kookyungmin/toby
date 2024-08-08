package net.happykoo.toby.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetStrategy<T> {
    T extractResultSet(ResultSet rs) throws SQLException;
}
