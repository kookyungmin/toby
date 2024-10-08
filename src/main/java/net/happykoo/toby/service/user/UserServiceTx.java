package net.happykoo.toby.service.user;

import net.happykoo.toby.dto.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class UserServiceTx implements UserService {
    private UserService userService;
    private PlatformTransactionManager transactionManager;

    public UserServiceTx(UserService userService,
                         PlatformTransactionManager transactionManager) {
        this.userService = userService;
        this.transactionManager = transactionManager;
    }

    @Override
    public void add(User user) {
        userService.add(user);
    }

    @Override
    public void upgradeLevels() {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            userService.upgradeLevels();
            this.transactionManager.commit(status);
        } catch(RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void update(User user) {

    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findById(String id) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
