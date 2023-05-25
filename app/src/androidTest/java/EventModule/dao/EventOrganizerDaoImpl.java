package EventModule.dao;

import java.util.List;
import database.DatabaseHelper;
import java.sql.ResultSet;
import java.sql.SQLException;

import EventModule.models.EventOrganizers;

public class EventOrganizerDaoImpl implements EventOrganizerDao {

    private final DatabaseHelper dbHelper = new DatabaseHelper();

    @Override
    public boolean addHelper(EventOrganizers helper)
    {
        String sql = "INSERT INTO Pasakuma_Rikotajs (pasakums_ID, lietotajs_ID, rikotajs, paligs) VALUES (?, ?, ?, ?)";
        return dbHelper.executeUpdate(sql, helper.getPasakuma_ID(), helper.getLietotaja_ID(), helper.getIrRikotajs(), helper.getIrPaligs()) > 0;
    }

    @Override
    public boolean removeHelper(int pasakumaId, int lietotajaId) {
        String sql = "DELETE FROM Pasakuma_Rikotajs WHERE pasakums_ID = ? AND lietotajs_ID = ?";
        return dbHelper.executeUpdate(sql, pasakumaId, lietotajaId) > 0;
    }

    @Override
    public List<EventOrganizers> getEventHelpers(int pasakumaId) {
        String sql = "SELECT * FROM Pasakuma_Rikotajs WHERE pasakums_ID = ?";
        return dbHelper.executeQueryForList(sql, this::mapToEventOrganizer, pasakumaId);
    }

    private EventOrganizers mapToEventOrganizer(ResultSet resultSet){
        EventOrganizers eventOrganizer = new EventOrganizers();
        try{
            eventOrganizer.setPasakuma_ID(resultSet.getInt("pasakums_ID"));
            eventOrganizer.setLietotaja_ID(resultSet.getInt("lietotajs_ID"));
            eventOrganizer.setIrRikotajs(resultSet.getBoolean("rikotajs"));
            eventOrganizer.setIrPaligs(resultSet.getBoolean("paligs"));
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return eventOrganizer;
    }
}
