package EventModule.dao;

import EventModule.models.*;

public interface EventDao {
    Event getEvent(int id);
    int createEvent(Event event, int userId);
    boolean deleteEvent(int id);
}
