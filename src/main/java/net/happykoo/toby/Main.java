package net.happykoo.toby;

import net.happykoo.toby.dao.UserDao;
import net.happykoo.toby.dto.User;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try {
            UserDao userDao = new UserDao();
            User user = userDao.findById(1);

            String nickName = Optional.ofNullable(user)
                    .map(User::getNickName)
                    .orElse(null);

            System.out.println("user nickName >> " + nickName);

            int totalCount = userDao.findAll().size();

            System.out.println("user all count >> " + totalCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
