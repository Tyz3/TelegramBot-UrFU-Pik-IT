package ru.DailyProblemBot.services;

import ru.DailyProblemBot.dao.UserDao;
import ru.DailyProblemBot.models.User;

import java.util.List;

public class UserService {

    private static final UserDao usersDao = new UserDao();

    public static User findUser(int id) {
        return usersDao.findById(id);
    }

    public static User findUser(String emailOrStudentId, String password) {
        User user = usersDao.findByEmailOrStudentId(emailOrStudentId);

        if (user == null) {
            return null;
        }

        return user.getPassword().equals(password) ? user : null;
    }

    public static void saveUser(User user) {
        usersDao.save(user);
    }

    public static void deleteUser(User user) {
        usersDao.delete(user);
    }

    public static void updateUser(User user) {
        usersDao.update(user);
    }

    public static List<User> findAllUsers() {
        return usersDao.findAll();
    }

}
