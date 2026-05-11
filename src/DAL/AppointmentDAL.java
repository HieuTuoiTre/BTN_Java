package DAL;

import BLL.DBConnection;
import DTO.Appointment;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AppointmentDAL {
    public static List<Appointment> getAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM Appointments";
        
        try (Connection conn = BLL.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentId(rs.getInt("appointment_id"));
                    appointment.setName(rs.getString("name"));
                    appointment.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    appointment.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());

                    list.add(appointment); 
                }
            }
        } catch (SQLException e) {}
        return list;
    }

    public static HashSet<Integer> getAppointedDays(int month, int year){
        HashSet<Integer> daysWithAppointment = new HashSet<Integer>();
        String sql = "SELECT DISTINCT DAY(start_time) AS day FROM Appointments " +
                     "WHERE MONTH(start_time) = ? AND YEAR(start_time) = ?";

        try (
            Connection conn = BLL.DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ){
            stmt.setInt(1, month);
            stmt.setInt(2, year);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()){
                    daysWithAppointment.add(rs.getInt("day"));
                }
            }
        } catch (SQLException e){
             e.printStackTrace();
        }
        return daysWithAppointment;
    }

    public static Appointment getAppointmentById(int appointmentId) {
        Appointment appointment = null;
        String sql = "SELECT * FROM Appointments WHERE appointment_id = ?";

        try (Connection conn = BLL.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    appointment = new Appointment();
                    appointment.setAppointmentId(rs.getInt("appointment_id"));
                    appointment.setName(rs.getString("name"));
                    appointment.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    appointment.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }


    public static List<Appointment> getAppointmentsByDate(java.time.LocalDate date) {
        List<Appointment> list = new ArrayList<>();

        String sql = "SELECT * FROM Appointments WHERE DATE(start_time) = ? ORDER BY start_time ASC";

        try (Connection conn = BLL.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setDate(1, java.sql.Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentId(rs.getInt("appointment_id"));
                    appointment.setName(rs.getString("name"));
                    appointment.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    appointment.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());

                    list.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int insertAppointment(Appointment appointment) {
        String sql = "INSERT INTO Appointments (name, start_time, end_time) VALUES (?, ?, ?)";

        try (Connection con = BLL.DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, appointment.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(appointment.getStartTime()));
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getEndTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);

                        appointment.setAppointmentId(newId);

                        return newId;
                    }
                }
            }
            return -1;
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    
    public static boolean updateAppointment(Appointment appointment) {
        String sql = "UPDATE Appointments SET name = ?, start_time = ?, end_time = ? WHERE appointment_id = ?";
        
        try (Connection conn = BLL.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, appointment.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(appointment.getStartTime()));
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getEndTime()));
            stmt.setInt(4, appointment.getAppointmentId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            return false;
        }
    }
    
    public static boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM Appointments WHERE appointment_id = ?";
        try (Connection conn = BLL.DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, appointmentId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            return false;
        }
    }


}
