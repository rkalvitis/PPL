package userAuthentication.dbo;

import database.DatabaseHelper;
import serviceprovider.dao.ServiceDao;
import serviceprovider.dao.ServiceDaoImpl;
import serviceprovider.models.Service;
import userAuthentication.models.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBHelper implements UserDbo {
    private final DatabaseHelper dbHelper = new DatabaseHelper();

    //user registration
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
    @Override
    public List<User> getAllUsersForEvent(long pasakumsId){
        String sql = "select Lietotajs.* from Pasakuma_Rikotajs " +
                "inner join Lietotajs on Lietotajs.lietotajs_ID = Pasakuma_Rikotajs.lietotajs_ID where Pasakuma_Rikotajs.pasakums_ID = ?";
        return dbHelper.executeQueryForList(sql, this::mapToUser, pasakumsId);
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