package users.dao;

import database.DatabaseHelper;
import users.models.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final DatabaseHelper dbHelper = new DatabaseHelper();

    @Override
    public User getUser(int id) {
        String sql = "SELECT * FROM Lietotajs WHERE lietotajs_ID = ?";
        return dbHelper.executeQuery(sql, this::mapToUser, id);
    }
    public boolean insertUser(User user) {
        String sql = "INSERT INTO Lietotajs (vards, uzvards, epasts, telefons, Parole, isAdmin, valstsKods) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return dbHelper.executeUpdate(sql, user.getVards(), user.getUzvards(), user.getEpasts(), user.getTelefons(), user.getParole(), user.getIsAdmin(), user.getValstsKods()) > 0;
    }
    public User findUserByEmail(String email){
        String sql = "SELECT * FROM Lietotajs WHERE epasts = ?";
        return dbHelper.executeQuery(sql, this::mapToUser, email);
    }
    public User findRegisteredUser(String email, String password){
        String sql = "SELECT * FROM Lietotajs WHERE epasts = ? AND Parole = ?";
        return dbHelper.executeQuery(sql, this::mapToUser, email, password);
    }
    public List<User> getAllUsersWithIsAdminZero() {
        String sql = "SELECT * FROM Lietotajs WHERE isAdmin = 0";
        return dbHelper.executeQueryForList(sql, this::mapToUser);
    }
    public boolean updateUser(User user) {
        String sql = "UPDATE Lietotajs SET vards = ?, uzvards = ?, epasts = ?, telefons = ?, valstsKods = ? WHERE lietotajs_ID = ?";
        return dbHelper.executeUpdate(sql, user.getVards(), user.getUzvards(), user.getEpasts(), user.getTelefons(), user.getValstsKods(), user.getLietotajs_ID()) > 0;
    }
    @Override
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE Lietotajs SET Parole = ? WHERE lietotajs_ID = ?";
        return dbHelper.executeUpdate(sql, newPassword, userId) > 0;
    }
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM Lietotajs WHERE lietotajs_ID = ?";
        return dbHelper.executeUpdate(sql, id) > 0;
    }
    private User mapToUser(ResultSet resultSet) {
        User user = new User();
        try {
            user.setLietotajs_ID(resultSet.getInt("lietotajs_ID"));
            user.setVards(resultSet.getString("vards"));
            user.setUzvards(resultSet.getString("uzvards"));
            user.setEpasts(resultSet.getString("epasts"));
            int telefons = resultSet.getInt("telefons");
            user.setTelefons(telefons != 0 ? telefons : null);
            user.setParole(resultSet.getString("Parole"));
            user.setIsAdmin(resultSet.getInt("isAdmin"));
            user.setValstsKods(resultSet.getInt("valstsKods"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}