package userAuthentication.dbo;

import userAuthentication.models.User;

public interface UserDbo {

        User getUser(int id);
        boolean insertUser(User user);
        User findUserByEmail(String email);

        User findRegisteredUser(String email, String password);
        //boolean updateUser(User user);
}
