package net.happykoo.toby.dao;

import net.happykoo.toby.dto.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    //User 삽입
    public void add(User user) throws ClassNotFoundException, SQLException {
        try (Connection conn = this.connectionMaker.getConnection();
             PreparedStatement ps = conn.prepareStatement("insert into User(id, nick_name) values(?, ?)")) {

            ps.setString(1, user.getId());
            ps.setString(2, user.getNickName());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    // ID 로 User 조회
    public User findById(String id) throws ClassNotFoundException, SQLException {
        try (Connection conn = this.connectionMaker.getConnection();
             PreparedStatement ps = conn.prepareStatement("select id, nick_name from User where id = ?")) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return User.builder()
                    .id(rs.getString("id"))
                    .nickName(rs.getString("nick_name"))
                    .build();
        } catch (SQLException e) {
            throw e;
        }
    }

    // ID 로 유저 삭제
    public void deleteById(String id) throws ClassNotFoundException, SQLException {
        try (Connection conn = this.connectionMaker.getConnection();
             PreparedStatement ps = conn.prepareStatement("delete from User where id = ?")) {

            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }

    }
}
