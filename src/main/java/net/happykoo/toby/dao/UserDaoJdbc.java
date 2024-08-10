package net.happykoo.toby.dao;

import net.happykoo.toby.dto.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // ID 로 User 조회
    @Override
    public User findById(String id) {
        RowMapper<User> userMapper = (rs, rowNum) -> User.builder()
                .id(rs.getString("id"))
                .nickName(rs.getString("nick_name"))
                .build();
        return jdbcTemplate.queryForObject("select id, nick_name from User where id = ?", new Object[] { id }, userMapper);
    }

    //User 삽입
    @Override
    public void add(User user) {
        jdbcTemplate.update("insert into User(id, nick_name) values(?, ?)", user.getId(), user.getNickName());
    }

    // ID 로 유저 삭제
    @Override
    public void deleteById(String id) {
        jdbcTemplate.update("delete from User where id = ?", id);
    }
}