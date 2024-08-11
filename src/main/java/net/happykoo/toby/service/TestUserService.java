package net.happykoo.toby.service;

import lombok.Getter;
import lombok.Setter;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Setter
@Getter
public class TestUserService extends UserService {
    private String errorUserId;
    public TestUserService(UserDao userDao,
                           PlatformTransactionManager transactionManager) {
        super(userDao, transactionManager);
    }

    @Override
    public void upgradeLevel(User user) {
        if (user.getId().equals(errorUserId)) {
            throw new DataAccessResourceFailureException("Network error");
        }
        super.upgradeLevel(user);
    }
}
