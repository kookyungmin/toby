package net.happykoo.toby.service;

import net.happykoo.toby.dto.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
