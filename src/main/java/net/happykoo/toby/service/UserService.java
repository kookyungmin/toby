package net.happykoo.toby.service;

import net.happykoo.toby.dto.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService {
    void add(User user);
    void upgradeLevels();
    void update(User user);
    @Transactional(readOnly = true)
    List<User> findAll();
    @Transactional(readOnly = true)
    User findById(String id);
    void deleteAll();
}
