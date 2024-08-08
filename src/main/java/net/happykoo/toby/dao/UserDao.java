package net.happykoo.toby.dao;

import net.happykoo.toby.dto.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Objects;

public class UserDao {
    private DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // ID 로 User 조회
    public User findById(String id) throws SQLException {
        return (User) jdbcContextWithSelectOneStatementStrategy((conn) -> {
            PreparedStatement ps = conn.prepareStatement("select id, nick_name from User where id = ?");
            ps.setString(1, id);

            return ps;
        }, (rs) -> {
            if (!rs.next()) throw new EmptyResultDataAccessException(1);

            return User.builder()
                    .id(rs.getString("id"))
                    .nickName(rs.getString("nick_name"))
                    .build();
        });
    }

    //User 삽입
    public void add(User user) throws SQLException {
        jdbcContextWithUpdateStatementStrategy(c -> {
            PreparedStatement ps = c.prepareStatement("insert into User(id, nick_name) values(?, ?)");

            ps.setString(1, user.getId());
            ps.setString(2, user.getNickName());

            return ps;
        });
    }

    // ID 로 유저 삭제
    public void deleteById(String id) throws SQLException {
        jdbcContextWithUpdateStatementStrategy(c -> {
            PreparedStatement ps = c.prepareStatement("delete from User where id = ?");
            ps.setString(1, id);
            return ps;
        });
    }

    public Object jdbcContextWithSelectOneStatementStrategy(StatementStrategy statementStrategy,
                                                            ResultSetStrategy resultSetStrategy) throws SQLException {
        ResultSet rs = null;

        try (Connection conn = this.dataSource.getConnection();
             PreparedStatement ps = statementStrategy.makePreparedStatement(conn)) {

            rs = ps.executeQuery();

            return resultSetStrategy.extractResultSet(rs);
        } catch (Exception e) {
            throw e;
        } finally {
            if (Objects.nonNull(rs)) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw e;
                }
            }
        }
    }

    public void jdbcContextWithUpdateStatementStrategy(StatementStrategy strategy) throws SQLException {
        try (Connection conn = this.dataSource.getConnection();
             PreparedStatement ps = strategy.makePreparedStatement(conn)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
}
