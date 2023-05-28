package EventModule.dao;

import java.util.List;

import EventModule.models.EventOrganizers;

public interface EventOrganizerDao {
    boolean addHelper(EventOrganizers helper);
    boolean removeHelper(int pasakumaId, int lietotajaId);
    List<EventOrganizers> getEventHelpers(int pasakumaId);
}
