package net.happykoo.toby.service;

import net.happykoo.toby.constant.Level;
import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private UserDao userDao;
    private DataSource dataSource;

    public UserService(UserDao userDao,
                       DataSource dataSource) {
        this.userDao = userDao;
        this.dataSource = dataSource;
    }

    public void add(User user) {
        user.setLevel(Level.BRONZE);
        userDao.add(user);
    }

    public void upgradeLevels() throws Exception {
        //트랜잭션 동기화 작업 초기화
        TransactionSynchronizationManager.initSynchronization();
        //트랜잭션 동기화 저장소에 connection 바인딩
        Connection connection = DataSourceUtils.getConnection(dataSource);

        try {
            connection.setAutoCommit(false); //트랜잭션 시작

            List<User> users = userDao.findAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            connection.commit(); //트랜잭션 커밋
        } catch (Exception e) {
            connection.rollback(); //트랜잭션 롤백
            throw e;
        } finally {
            //connection close
            DataSourceUtils.releaseConnection(connection, dataSource);
            //트랜잭션 동기화 작업 종료
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
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
