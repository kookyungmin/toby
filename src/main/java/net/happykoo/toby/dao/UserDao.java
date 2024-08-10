package net.happykoo.toby.dao;

import net.happykoo.toby.dto.User;

public interface UserDao {
    User findById(String id);
    void add(User user);
    void deleteById(String id);
}
