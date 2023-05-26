package EventModule.dao;

import database.DatabaseHelper;
import EventModule.models.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventDaoImpl implements EventDao {
    private final DatabaseHelper dbHelper = new DatabaseHelper();

    @Override
    public Event getEvent(int id) {
        String sql = "SELECT * FROM Pasakums WHERE pasakums_ID = ?";
        return dbHelper.executeQuery(sql, this::mapToEvent, id);
    }

    @Override
    public int createEvent(Event event, int userId){
        String eventSql = "INSERT INTO Pasakums (nosaukums, datumslaiks, atrasanas_vieta, foto_video) VALUES (?, ?, ?, ?)";
        String querySql = "SELECT * FROM Pasakums WHERE nosaukums = ? AND datumslaiks = ?";
        String helperSql = "INSERT INTO Pasakuma_Rikotajs (pasakums_ID, lietotajs_ID, rikotajs, paligs) VALUES (?, ?, ?, ?)";

        int eventAdded = dbHelper.executeUpdate(eventSql, event.getNosaukums(), event.getSakumaLaiks(), event.getLokacija(), event.getLinksUzFoto());
        if(eventAdded > 0){
            Event newEvent = dbHelper.executeQuery(querySql, this::mapToEvent, event.getNosaukums(), event.getSakumaLaiks());
            int helperAdded = dbHelper.executeUpdate(helperSql, newEvent.getPasakums_ID(), userId, 1, 0);
            if (helperAdded > 0) {
                return newEvent.getPasakums_ID();
            }
        }

        return 0;
    }

    @Override
    public boolean deleteEvent(int id)
    {
        String sql = "DELETE FROM Pasakums WHERE pasakums_ID = ?";
        return dbHelper.executeUpdate(sql, id) > 0;
    }

    private Event mapToEvent(ResultSet resultSet) {
        Event event = new Event();
        try{
            event.setPasakums_ID(resultSet.getInt("pasakums_ID"));
            event.setNosaukums(resultSet.getString("nosaukums"));
            event.setSakumaLaiks(resultSet.getTimestamp("datumslaiks"));
            event.setLokacija((resultSet.getString("atrasanas_vieta")));
            event.setLinksUzFoto(resultSet.getString("foto_video"));
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return event;
    }
}