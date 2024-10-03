package net.happykoo.toby.service.sql;

import net.happykoo.toby.dto.SqlInfo;

import java.util.List;

public interface SqlLoader {
    List<SqlInfo> load();
}
