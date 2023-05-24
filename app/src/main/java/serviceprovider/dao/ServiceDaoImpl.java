package serviceprovider.dao;

import database.DatabaseHelper;
import serviceprovider.models.Service;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceDaoImpl implements ServiceDao {
    private final DatabaseHelper dbHelper = new DatabaseHelper();

    @Override
    public Service getServiceProvider(int id) {
        String sql = "SELECT * FROM Servisa_sniedzejs WHERE servisasniedzejs_ID = ?";
        return dbHelper.executeQuery(sql, this::mapToService, id);
    }

    @Override
    public boolean createServiceProvider(Service service) {
        String sql = "INSERT INTO Servisa_sniedzejs (nosaukums, epasts, pilseta, adrese, telefons, parole) VALUES (?, ?, ?, ?, ?, ?)";
        return dbHelper.executeUpdate(sql, service.getNosaukums(), service.getEpasts(), service.getPilseta(), service.getAdrese(), service.getTelefons(), service.getParole()) > 0;
    }

    @Override
    public boolean updateServiceProvider(Service service) {
        String sql = "UPDATE Servisa_sniedzejs SET nosaukums = ?, epasts = ?, pilseta = ?, adrese = ?, telefons = ?, parole = ? WHERE servisasniedzejs_ID = ?";
        return dbHelper.executeUpdate(sql, service.getNosaukums(), service.getEpasts(), service.getPilseta(), service.getAdrese(), service.getTelefons(), service.getParole(), service.getServisasniedzejs_ID()) > 0;
    }

    @Override
    public boolean deleteServiceProvider(int id) {
        String sql = "DELETE FROM Servisa_sniedzejs WHERE servisasniedzejs_ID = ?";
        return dbHelper.executeUpdate(sql, id) > 0;
    }

    private Service mapToService(ResultSet resultSet) {
        Service service = new Service();
        try {
            service.setServisasniedzejs_ID(resultSet.getInt("servisasniedzejs_ID"));
            service.setNosaukums(resultSet.getString("nosaukums"));
            service.setEpasts(resultSet.getString("epasts"));
            service.setPilseta(resultSet.getString("pilseta"));
            service.setAdrese(resultSet.getString("adrese"));
            service.setTelefons(resultSet.getString("telefons"));
            service.setParole(resultSet.getString("parole"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return service;
    }
}