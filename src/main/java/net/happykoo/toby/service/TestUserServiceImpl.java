package net.happykoo.toby.service;

import lombok.Getter;
import lombok.Setter;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;
import org.springframework.dao.DataAccessResourceFailureException;

@Setter
@Getter
public class TestUserServiceImpl extends UserServiceImpl {
    private String errorUserId;
    public TestUserServiceImpl(UserDao userDao) {
        super(userDao);
    }

    @Override
    public void upgradeLevel(User user) {
        if (user.getId().equals(errorUserId)) {
            throw new DataAccessResourceFailureException("Network error");
        }
        super.upgradeLevel(user);
    }
}
