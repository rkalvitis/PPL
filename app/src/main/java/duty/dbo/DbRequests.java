package duty.dbo;

import duty.models.Duty;
import database.DatabaseHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DbRequests  implements IDbRequests{
    private final DatabaseHelper dbHelper = new DatabaseHelper();
    @Override
    public boolean deleteDuty(long id){
        String sql = "DELETE FROM Pienakums WHERE pienakums_ID = ?";
        return dbHelper.executeUpdate(sql, id) > 0;
    }
    @Override
    public boolean updateDuty(Duty duty){
        String sql = "UPDATE Pienakums SET nosaukums = ?, apraksts = ?, paveikts = ?";
        return dbHelper.executeUpdate(sql, duty.getNosaukums(), duty.getApraksts(), duty.isPaveikts()) > 0;
    }
    @Override
    public boolean createDuty(Duty duty){
        String sql = "INSERT INTO Pienakums (nosaukums, apraksts, paveikts) VALUES (?, ?, ?)";
        return dbHelper.executeUpdate(sql, duty.getNosaukums(), duty.getApraksts(), duty.isPaveikts()) > 0;
    }
    @Override
    public List<Duty> getAllDuties(long pasakumsId){
        String sql = "select Pienakums.pienakums_ID, Pienakums.nosaukums, Pienakums.apraksts, Pienakums.paveikts from Pasakums \n" +
            "inner join Pasakuma_Rikotajs on Pasakuma_Rikotajs.pasakums_ID  = Pasakums.pasakums_ID \n" +
            "inner join Pienakumi_Lietotajam on Pienakumi_Lietotajam.Pasakuma_Rikotajs_ID  = Pasakuma_Rikotajs.Pasakuma_Rikotajs_ID \n" +
            "inner join Pienakums on Pienakums.pienakums_ID  = Pienakumi_Lietotajam.pienakums_ID \n" +
            "where Pasakums.pasakums_ID = ?";
        return dbHelper.executeQueryForList(sql, this::mapToDuty, pasakumsId);
    }
    @Override
    public Duty getDuty(long pienakumsId){
        String sql = "select * from Pienakums where Pienakums.pienakums_ID = ?";
        return dbHelper.executeQuery(sql, this::mapToDuty, pienakumsId);
    }
    @Override
    public boolean changeDutyStatus(long pienakumsId, boolean isPaveikts){
        String sql = "UPDATE Pienakums SET paveikts = ? where pienakums_ID = ?";
        return dbHelper.executeUpdate(sql, isPaveikts, pienakumsId) > 0;
    }

    private Duty mapToDuty(ResultSet resultSet){
        Duty duty = new Duty();
        try{
            duty.setPienakums_ID(resultSet.getInt("pienakums_ID"));
            duty.setNosaukums(resultSet.getString("nosaukums"));
            duty.setApraksts(resultSet.getString("apraksts"));
            duty.setPaveikts(resultSet.getBoolean("paveikts"));
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return duty;
    }
}