package net.happykoo.toby.dao;

import net.happykoo.toby.constant.Level;
import net.happykoo.toby.dto.User;
import net.happykoo.toby.service.sql.SqlService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private SqlService sqlService;

    public UserDaoJdbc(DataSource dataSource,
                       SqlService sqlService) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.sqlService = sqlService;
    }

    //전체 User 조회
    @Override
    public List<User> findAll() {
        return this.jdbcTemplate.query(sqlService.getSql("user.findAll")
                , getUserRowMapper());
    }

    //ID 로 User 조회
    @Override
    public User findById(String id) {
        return this.jdbcTemplate.queryForObject(sqlService.getSql("user.findById")
                , new Object[] { id }
                , getUserRowMapper());
    }

    //User 삽입
    @Override
    public void add(User user) {
        this.jdbcTemplate.update(sqlService.getSql("user.add")
                , user.getId()
                , user.getNickName()
                , user.getLevel().getIntValue()
                , user.getLoginCount()
                , user.getRecommendCount());
    }

    //User 정보 수정
    @Override
    public void update(User user) {
        this.jdbcTemplate.update(sqlService.getSql("user.update")
                , user.getNickName()
                , user.getLevel().getIntValue()
                , user.getLoginCount()
                , user.getRecommendCount()
                , user.getId());
    }

    //ID 로 유저 삭제
    @Override
    public void deleteById(String id) {
        this.jdbcTemplate.update(sqlService.getSql("user.deleteById"), id);
    }

    //전체 유저 삭제
    @Override
    public void deleteAll() {
        this.jdbcTemplate.update(sqlService.getSql("user.deleteAll"));
    }

    private RowMapper<User> getUserRowMapper() {
        return (rs, rowNum) -> User.builder()
                .id(rs.getString("id"))
                .nickName(rs.getString("nick_name"))
                .level(Level.valueOf(rs.getInt("level")))
                .loginCount(rs.getInt("login_count"))
                .recommendCount(rs.getInt("recommend_count"))
                .build();
    }
}