package users.dao;

import users.models.User;

public interface UserDao {

        User getUser(int id);
        boolean insertUser(User user);
        User findUserByEmail(String email);
        User findRegisteredUser(String email, String password);
        boolean updateUser(User user);
        boolean updatePassword(int userId, String newPassword);
        boolean deleteUser(int id);
}
