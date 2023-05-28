package EventModule.dao;

import database.DatabaseHelper;
import EventModule.models.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class EventDaoImpl implements EventDao {
    private final DatabaseHelper dbHelper = new DatabaseHelper();

    @Override
    public Event getEvent(int id) {
        String sql = "SELECT * FROM Pasakums WHERE pasakums_ID = ?";
        return dbHelper.executeQuery(sql, this::mapToEvent, id);
    }

    @Override
    public boolean createEvent(Event event, int userId){
        String eventSql = "INSERT INTO Pasakums (nosaukums, datumslaiks, atrasanas_vieta, foto_video) VALUES (?, ?, ?, ?)";
        String querySql = "SELECT * FROM Pasakums WHERE nosaukums = ? AND datumslaiks = ?";
        String helperSql = "INSERT INTO Pasakuma_Rikotajs (pasakums_ID, lietotajs_ID, rikotajs, paligs) VALUES (?, ?, ?, ?)";

        int eventAdded = dbHelper.executeUpdate(eventSql, event.getNosaukums(), event.getSakumaLaiks(), event.getLokacija(), event.getLinksUzFoto());
        if(eventAdded > 0){
            Event newEvent = dbHelper.executeQuery(querySql, this::mapToEvent, event.getNosaukums(), event.getSakumaLaiks());
            int helperAdded = dbHelper.executeUpdate(helperSql, newEvent.getPasakums_ID(), userId, 1, 0);
            if (helperAdded > 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deleteEvent(int id)
    {
        String sql = "DELETE FROM Pasakums WHERE pasakums_ID = ?";
        return dbHelper.executeUpdate(sql, id) > 0;
    }

    @Override
    public List<Event> getAllEvents() {
        String sql = "SELECT * FROM PASAKUMS";
        return dbHelper.executeQueryForList(sql, this::mapToEvent);
    }

    private Event mapToEvent(ResultSet resultSet) {
        Event event = new Event();
        try{
            event.setPasakums_ID(resultSet.getInt("pasakums_ID"));
            event.setNosaukums(resultSet.getString("nosaukums"));
            event.setSakumaLaiks(resultSet.getDate("datumslaiks"));
            event.setLokacija((resultSet.getString("atrasanas_vieta")));
            event.setLinksUzFoto(resultSet.getString("foto_video"));
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return event;
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
}