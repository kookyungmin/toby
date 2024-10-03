package net.happykoo.toby.test.sql;


import lombok.extern.slf4j.Slf4j;
import net.happykoo.toby.service.sql.SqlRetrievalFailureException;
import net.happykoo.toby.service.sql.BaseSqlService;
import net.happykoo.toby.service.sql.XmlSqlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Slf4j
public class XmlSqlServiceTest {
    private Map<String, String> testSqlMap = new HashMap<>();
    private XmlSqlService xmlSqlService;

    @BeforeEach
    public void setup() {
        xmlSqlService = new XmlSqlService("/sql/sql-mapper.xml");
        xmlSqlService.onload();

        testSqlMap.put("user.findAll", "select id, nick_name, level, login_count, recommend_count from User where 1 = 1");
        testSqlMap.put("user.findById", "select id, nick_name, level, login_count, recommend_count  from User where id = ?");
        testSqlMap.put("user.add", "insert into User(id, nick_name, level, login_count, recommend_count) values(?, ?, ?, ?, ?)");
        testSqlMap.put("user.update", "update User set nick_name = ?, level = ?, login_count = ?, recommend_count = ? where id = ?");
        testSqlMap.put("user.deleteById", "delete from User where id = ?");
        testSqlMap.put("user.deleteAll", "delete from User where 1 = 1");
    }

    @Test
    @DisplayName("getSql 테스트 :: 정상적으로 조회되는 경우")
    public void getSqlTest() {
        testSqlMap.forEach((key, value) -> {
            assertEquals(value, xmlSqlService.getSql(key));
        });
    }

    @Test
    @DisplayName("getSql 테스트 :: 없는 key로 조회하는 경우")
    public void getSqlFailTest() {
        assertThrows(SqlRetrievalFailureException.class, () -> xmlSqlService.getSql("TEST"));
    }
}
