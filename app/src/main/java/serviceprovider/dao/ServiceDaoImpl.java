package serviceprovider.dao;

import database.DatabaseHelper;
import serviceprovider.models.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ServiceDaoImpl implements ServiceDao {
    private final DatabaseHelper dbHelper = new DatabaseHelper();

    @Override
    public Service getServiceProvider(int id) {
        String sql = "SELECT * FROM Servisa_sniedzejs WHERE servisasniedzejs_ID = ?";
        return dbHelper.executeQuery(sql, this::mapToService, id);
    }
    public Service findRegisteredService (String email, String password){
        String sql = "SELECT * FROM Servisa_sniedzejs WHERE epasts = ? AND parole = ?";
        return dbHelper.executeQuery(sql, this::mapToService, email, password);
    }
    public Service findServiceByEmail(String email){
        String sql = "SELECT * FROM Servisa_sniedzejs WHERE epasts = ?";
        return dbHelper.executeQuery(sql, this::mapToService, email);
    }
    @Override
    public boolean createServiceProvider(Service service) {
        String sql = "INSERT INTO Servisa_sniedzejs (nosaukums, epasts, pilseta, adrese, telefons, parole, valsts_kods) VALUES (?, ?, ?,?, ?, ?, ?)";
        return dbHelper.executeUpdate(sql, service.getNosaukums(), service.getEpasts(), service.getPilseta(), service.getAdrese(), service.getTelefons(), service.getParole(), service.getValstsKods()) > 0;
    }

    @Override
    public boolean updateServiceProvider(Service service) {
        String sql = "UPDATE Servisa_sniedzejs SET nosaukums = ?, epasts = ?, pilseta = ?, adrese = ?, telefons = ?, valsts_kods = ? WHERE servisasniedzejs_ID = ?";
        return dbHelper.executeUpdate(sql, service.getNosaukums(), service.getEpasts(), service.getPilseta(), service.getAdrese(), service.getTelefons(), service.getValstsKods(), service.getServisasniedzejs_ID()) > 0;
    }
    @Override
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE Servisa_sniedzejs SET parole = ? WHERE servisasniedzejs_ID = ?";
        return dbHelper.executeUpdate(sql, newPassword, userId) > 0;
    }
    @Override
    public boolean deleteServiceProvider(int id) {
        String sql = "DELETE FROM Servisa_sniedzejs WHERE servisasniedzejs_ID = ?";
        return dbHelper.executeUpdate(sql, id) > 0;
    }
    public List<Service> getAllServices() {
        String sql = "SELECT * FROM Servisa_sniedzejs";
        return dbHelper.executeQueryForList(sql, this::mapToService);
    }

    private Service mapToService(ResultSet resultSet) {
        Service service = new Service();
        try {
            service.setServisasniedzejs_ID(resultSet.getInt("servisasniedzejs_ID"));
            service.setNosaukums(resultSet.getString("nosaukums"));
            service.setEpasts(resultSet.getString("epasts"));
            service.setPilseta(resultSet.getString("pilseta"));
            service.setAdrese(resultSet.getString("adrese"));
            service.setTelefons(resultSet.getInt("telefons"));
            service.setParole(resultSet.getString("parole"));
            service.setValstsKods(resultSet.getInt("valsts_kods"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return service;
    }
}