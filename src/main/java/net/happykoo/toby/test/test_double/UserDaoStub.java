package net.happykoo.toby.test.test_double;

import net.happykoo.toby.constant.Level;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;

import java.util.List;

public class UserDaoStub implements UserDao {
    @Override
    public User findById(String id) {
        return User.builder()
                .id("happykoo")
                .level(Level.BRONZE)
                .build();
    }

    @Override
    public void add(User user) {

    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void deleteAll() {

    }
}
