package serviceprovider.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import serviceprovider.models.Booking;
import database.DatabaseHelper;

public class BookingDaoImpl implements BookingDao {
    private final DatabaseHelper dbHelper = new DatabaseHelper();

    @Override
    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO Sniedzeja_rezervesana (datumsNo, datumsLidz, servisasniedzejs_ID) VALUES (?, ?, ?)";
        return dbHelper.executeUpdate(sql, convertDateFormat(booking.getDatumsNo().toString()), convertDateFormat(booking.getDatumsLidz().toString()), booking.getServisasniedzejs_ID()) > 0;
    }

    @Override
    public boolean deleteBooking(int id) {
        String sql = "DELETE FROM Sniedzeja_rezervesana WHERE rezervacijas_ID = ?";
        return dbHelper.executeUpdate(sql, id) > 0;
    }

    @Override
    public List<Booking> getAllBookings() {
        String sql = "SELECT * FROM Sniedzeja_rezervesana";
        return dbHelper.executeQueryForList(sql, this::mapToBooking);
    }

    private Booking mapToBooking(ResultSet resultSet) {
        Booking booking = new Booking();
        try {
            booking.setRezervacijas_ID(resultSet.getInt("rezervacijas_ID"));
            booking.setServisasniedzejs_ID(resultSet.getInt("servisasniedzejs_ID"));
            booking.setDatumsNo(resultSet.getDate("datumsNo"));
            booking.setDatumsLidz(resultSet.getDate("datumsLidz"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingsByServisasniedzejs_ID(int servisasniedzejs_ID) {
        String sql = "SELECT * FROM Sniedzeja_rezervesana WHERE servisasniedzejs_ID = ?";
        return dbHelper.executeQueryForList(sql, this::mapToBooking, servisasniedzejs_ID);
    }

    @Override
    public boolean bookServiceProvider(Booking booking) {
        return createBooking(booking);
    }

    public List<Date> getBookedDates(int servisasniedzejs_ID) {
        // Get all bookings for the service provider
        List<Booking> bookings = getBookingsByServisasniedzejs_ID(servisasniedzejs_ID);

        List<Date> bookedDates = new ArrayList<>();

        for (Booking booking : bookings) {
            // Get the start and end dates of the booking
            Date startDate = booking.getDatumsNo();
            Date endDate = booking.getDatumsLidz();

            // Iterate through each day in the booking period
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            while (!calendar.getTime().after(endDate)) {
                // Add this day to the bookedDates
                bookedDates.add(calendar.getTime());

                // Move the calendar to the next day
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        return bookedDates;
    }


    public String convertDateFormat(String dateStr){
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'XXX yyyy", Locale.ENGLISH);
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        // Parse to a Date object
        Date date = null;
        try{
            date = inputFormat.parse(dateStr);
        }catch (Exception e){
            e.printStackTrace();
        }

        // Output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Format to a string
        return outputFormat.format(date);
    }
}
