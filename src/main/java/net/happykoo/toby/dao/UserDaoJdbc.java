package net.happykoo.toby.dao;

import net.happykoo.toby.constant.Level;
import net.happykoo.toby.dto.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //전체 User 조회
    @Override
    public List<User> findAll() {
        return this.jdbcTemplate.query("select id, nick_name, level, login_count, recommend_count from User where 1 = 1"
                , getUserRowMapper());
    }

    //ID 로 User 조회
    @Override
    public User findById(String id) {
        return this.jdbcTemplate.queryForObject("select id, nick_name, level, login_count, recommend_count  from User where id = ?"
                , new Object[] { id }
                , getUserRowMapper());
    }

    //User 삽입
    @Override
    public void add(User user) {
        this.jdbcTemplate.update("insert into User(id, nick_name, level, login_count, recommend_count) values(?, ?, ?, ?, ?)"
                , user.getId()
                , user.getNickName()
                , user.getLevel().getIntValue()
                , user.getLoginCount()
                , user.getRecommendCount());
    }

    //User 정보 수정
    @Override
    public void update(User user) {
        this.jdbcTemplate.update("update User set nick_name = ?, level = ?, login_count = ?, recommend_count = ? where id = ?"
                , user.getNickName()
                , user.getLevel().getIntValue()
                , user.getLoginCount()
                , user.getRecommendCount()
                , user.getId());
    }

    //ID 로 유저 삭제
    @Override
    public void deleteById(String id) {
        this.jdbcTemplate.update("delete from User where id = ?", id);
    }

    //전체 유저 삭제
    @Override
    public void deleteAll() {
        this.jdbcTemplate.update("delete from User where 1 = 1");
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