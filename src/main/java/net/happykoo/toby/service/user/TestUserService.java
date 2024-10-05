package net.happykoo.toby.service.user;

import lombok.Getter;
import lombok.Setter;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Service("testUserService")
public class TestUserService extends UserServiceImpl {
    public TestUserService(UserDao userDao) {
        super(userDao);
    }

    @Override
    public void upgradeLevel(User user) {
        if (user.getId().equals("test4")) {
            throw new DataAccessResourceFailureException("Network error");
        }
        super.upgradeLevel(user);
    }
}

