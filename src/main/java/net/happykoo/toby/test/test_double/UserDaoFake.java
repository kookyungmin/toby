package net.happykoo.toby.test.test_double;

import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoFake implements UserDao  {
    private Map<String, User> database = new HashMap<>();

    @Override
    public User findById(String id) {
        return database.get(id);
    }

    @Override
    public void add(User user) {
        database.put(user.getId(), user);
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
