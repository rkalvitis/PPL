package serviceprovider.dao;

import database.DatabaseHelper;
import serviceprovider.models.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
            service.setTelefons(resultSet.getString("telefons"));
            service.setValstsKods(resultSet.getInt("valsts_kods"));

            Integer average_rating = resultSet.getInt("average_rating");
            if (average_rating == null) {
                service.setReitings(0);
            } else {
                service.setReitings(average_rating);
            }

            Integer rating_count = resultSet.getInt("rating_count");
            if (average_rating == null) {
                service.setReitinguSkaits(0);
            } else {
                service.setReitinguSkaits(rating_count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return service;
    }

    public String convertDateFormat(String dateStr) throws Exception {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'XXX yyyy", Locale.ENGLISH);
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        // Parse to a Date object
        Date date = inputFormat.parse(dateStr);

        // Output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Format to a string
        return outputFormat.format(date);
    }

    @Override
    public List<Service> searchServices(String keyword, String category, Date dateFrom, Date dateTo, String orderBy) {
        StringBuilder sql = new StringBuilder("SELECT s.servisasniedzejs_ID, s.nosaukums, s.epasts, s.pilseta, s.adrese, s.telefons, s.valsts_kods, AVG(a.vertejums) AS average_rating, COUNT(a.vertejums) AS rating_count " +
                "FROM Servisa_sniedzejs s " +
                "LEFT JOIN Atsauksme_Servisam asr ON s.servisasniedzejs_ID = asr.servisasniedzejs_ID " +
                "LEFT JOIN Atsauksme a ON asr.atsauksmes_ID = a.atsauksmes_ID " +
                "LEFT JOIN Servisa_tips st ON s.servisasniedzejs_ID = st.servisasniedzejs_ID ");

        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if(!keyword.isEmpty()){
            conditions.add("s.nosaukums LIKE ?");
            params.add("%" + keyword + "%");
        }
        if(!category.isEmpty()){
            conditions.add("st.tips LIKE ?");
            params.add("%" + category + "%");
        }
        if(dateFrom != null && dateTo != null){
            conditions.add("( s.servisasniedzejs_ID NOT IN (SELECT sr.servisasniedzejs_ID FROM Sniedzeja_rezervesana sr " +
                    "WHERE sr.servisasniedzejs_ID = s.servisasniedzejs_ID " +
                    "AND sr.datumsNo BETWEEN ? AND ? " +
                    "OR sr.datumsLidz BETWEEN ? AND ?))");
            try{
                params.add(convertDateFormat(dateFrom.toString()));
                params.add(convertDateFormat(dateTo.toString()));
                params.add(convertDateFormat(dateFrom.toString()));
                params.add(convertDateFormat(dateTo.toString()));
            } catch(Exception e){
                e.printStackTrace();
            }
        } else if (dateFrom == null && dateTo != null) {
            dateFrom = new Date();
            conditions.add("( s.servisasniedzejs_ID NOT IN (SELECT sr.servisasniedzejs_ID FROM Sniedzeja_rezervesana sr " +
                "WHERE sr.servisasniedzejs_ID = s.servisasniedzejs_ID " +
                "AND sr.datumsNo BETWEEN (CONVERT(DATETIME, ?, 126)) AND (CONVERT(DATETIME, ?, 126)) " +
                "OR sr.datumsLidz BETWEEN (CONVERT(DATETIME, ?, 126)) AND (CONVERT(DATETIME, ?, 126))))");
            try{
                params.add(convertDateFormat(dateFrom.toString()));
                params.add(convertDateFormat(dateTo.toString()));
                params.add(convertDateFormat(dateFrom.toString()));
                params.add(convertDateFormat(dateTo.toString()));
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        sql.append(" GROUP BY s.servisasniedzejs_ID, s.nosaukums, s.epasts, s.pilseta, s.adrese, s.telefons, s.valsts_kods");

        if(orderBy != null){
            switch (orderBy) {
                case "name":
                    sql.append(" ORDER BY s.nosaukums");
                    break;
                case "rating":
                    sql.append(" ORDER BY average_rating DESC");
                    break;
                case "ratingCount":
                    sql.append(" ORDER BY rating_count DESC");
                    break;
                default:
                    sql.append(" ORDER BY s.nosaukums"); // default ordering
                    break;
            }
        }

        return dbHelper.executeQueryForList(sql.toString(), this::mapToService, params.toArray());
    }
}