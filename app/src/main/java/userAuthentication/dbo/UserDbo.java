package userAuthentication.dbo;

import java.util.List;

import userAuthentication.models.User;

public interface UserDbo {

        User getUser(int id);
        boolean insertUser(User user);
        User findUserByEmail(String email);

        User findRegisteredUser(String email, String password);
        List<User> getAllUsersForEvent(long pasakumsId);
        //boolean updateUser(User user);
}
