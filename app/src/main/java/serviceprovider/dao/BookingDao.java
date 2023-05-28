package serviceprovider.dao;

import serviceprovider.models.Booking;

import java.util.Date;
import java.util.List;

public interface BookingDao {
    boolean createBooking(Booking booking);

    boolean deleteBooking(int id);

    List<Booking> getAllBookings();

    List<Booking> getBookingsByServisasniedzejs_ID(int servisasniedzejs_ID);
    boolean bookServiceProvider(Booking booking);
    public List<Date> getBookedDates(int servisasniedzejs_ID);

}
