package net.happykoo.toby.service.sql;

import net.happykoo.toby.dto.SqlInfo;

import javax.annotation.PostConstruct;
import java.util.List;

public class BaseSqlService implements SqlService {
    private SqlLoader sqlLoader;
    private SqlRegistry sqlRegistry;

    public BaseSqlService(SqlLoader sqlLoader,
                          SqlRegistry sqlRegistry) {
        this.sqlLoader = sqlLoader;
        this.sqlRegistry = sqlRegistry;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return sqlRegistry.findById(key);
    }

    @PostConstruct
    public void onload() {
        List<SqlInfo> sqlInfoList = sqlLoader.load();
        for(SqlInfo sqlInfo : sqlInfoList) {
            sqlRegistry.register(sqlInfo);
        }
    }
}
