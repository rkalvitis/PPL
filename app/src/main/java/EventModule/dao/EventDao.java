package EventModule.dao;

import java.util.List;

import EventModule.models.*;

public interface EventDao {
    Event getEvent(int id);
    boolean createEvent(Event event, int userId);
    boolean deleteEvent(int id);
    List<Event> getAllEvents();
}
