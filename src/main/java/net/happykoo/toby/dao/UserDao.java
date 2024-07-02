package net.happykoo.toby.dao;

import net.happykoo.toby.dto.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public User findById(int id) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/happykoo", "root", "");
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

    public List<User> findAll() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/happykoo", "root", "");
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
