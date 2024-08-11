package net.happykoo.toby.service;

import net.happykoo.toby.constant.Level;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class UserService {
    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private UserDao userDao;
    private PlatformTransactionManager transactionManager;

    public UserService(UserDao userDao,
                       PlatformTransactionManager transactionManager) {
        this.userDao = userDao;
        this.transactionManager = transactionManager;
    }

    public void add(User user) {
        user.setLevel(Level.BRONZE);
        userDao.add(user);
    }

    public void upgradeLevels() {
        //트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.findAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            transactionManager.commit(status); //트랜잭션 커밋
        } catch (RuntimeException e) {
            transactionManager.rollback(status); //트랜잭션 롤백
            throw e;
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BRONZE: return (user.getLoginCount() >= MIN_LOGIN_COUNT_FOR_SILVER);
            case SILVER: return (user.getRecommendCount() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }
}
