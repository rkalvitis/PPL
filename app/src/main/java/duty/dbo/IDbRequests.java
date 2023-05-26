package duty.dbo;

import userAuthentication.models.User;
import duty.models.Duty;
import java.util.List;

public interface IDbRequests {
    boolean deleteDuty(long id);
    boolean updateDuty(Duty duty);
    boolean createDuty(Duty duty);
    List<Duty> getAllDuties(long pasakumsId);
    Duty getDuty(long pienakumsId);
    boolean changeDutyStatus(long pienakumsId, boolean isPaveikts);


}
