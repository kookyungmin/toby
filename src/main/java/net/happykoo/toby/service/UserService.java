package net.happykoo.toby.service;

import net.happykoo.toby.dto.User;

import java.util.List;

public interface UserService {
    void add(User user);
    void upgradeLevels();
    void update(User user);
    //추가
    List<User> findAll();
    User findById(String id);
    void deleteAll();
}
