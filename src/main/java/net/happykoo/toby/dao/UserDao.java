package net.happykoo.toby.dao;

import net.happykoo.toby.dto.User;

import java.util.List;

public interface UserDao {
    User findById(String id);
    void add(User user);
    void deleteById(String id);
    List<User> findAll();
    void update(User user);
    void deleteAll();
}
