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

    // ID 로 User 조회
    public User findById(int id) throws ClassNotFoundException, SQLException {
        try (Connection conn = this.connectionMaker.getConnection();
             PreparedStatement ps = conn.prepareStatement("select id, nickname from User where id = ?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return User.builder()
                    .id(rs.getInt("id"))
                    .nickName(rs.getString("nickname"))
                    .build();
        } catch (SQLException e) {
            throw e;
        }
    }

    // 전체 User 조회
    public List<User> findAll() throws ClassNotFoundException, SQLException {
        try (Connection conn = this.connectionMaker.getConnection();
             PreparedStatement ps = conn.prepareStatement("select id, nickname from User")) {

            List<User> userList = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                userList.add(User.builder()
                        .id(rs.getInt("id"))
                        .nickName(rs.getString("nickname"))
                        .build());
            }

            return userList;
        } catch (SQLException e) {
            throw e;
        }

    }
}
