package DAL;

import BLL.DBConnection;
import DTO.Reminder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderDAL {
    public static List<Reminder> getRemindersByCalendar_24H() {
        List<Reminder> list = new ArrayList<>();
        String sql = "SELECT DISTINCT r.* FROM Reminders r " +
                "JOIN Appointments a ON r.appointment_id = a.appointment_id " +
                "AND r.target_time BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 1 DAY) " +
                "ORDER BY r.target_time ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Reminder(
                            rs.getInt("reminder_id"),
                            rs.getInt("appointment_id"),
                            Reminder.ReminderType.valueOf(rs.getString("reminder_type")),
                            rs.getTimestamp("target_time").toLocalDateTime(),
                            rs.getString("message")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean insertReminder(Reminder reminder) {
        String sql = "INSERT INTO Reminders (appointment_id, reminder_type, target_time, message) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reminder.getAppointmentId());
            stmt.setString(2, String.valueOf(reminder.getReminderType()));
            stmt.setTimestamp(3, Timestamp.valueOf(reminder.getTargetTime()));
            stmt.setString(4, reminder.getMessage());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean updateReminder(Reminder reminder) {
        String sql = "UPDATE Reminders SET reminder_type = ?, target_time = ?, message = ? WHERE reminder_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(reminder.getReminderType()));
            stmt.setTimestamp(2, Timestamp.valueOf(reminder.getTargetTime()));
            stmt.setString(3, reminder.getMessage());
            stmt.setInt(4, reminder.getReminderId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean deleteReminder(int reminderId) {
        String sql = "DELETE FROM Reminders WHERE reminder_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reminderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean deleteRemindersByAppointmentId(int appointmentId) {
        String sql = "DELETE FROM Reminders WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }
    public static List<Reminder> getDueReminders() {
        List<Reminder> list = new ArrayList<>();
        // Lấy các reminder có target_time nhỏ hơn hoặc bằng hiện tại VÀ chưa thông báo
        String sql = "SELECT * FROM Reminders WHERE target_time <= NOW() AND is_notified = 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Reminder(
                        rs.getInt("reminder_id"),
                        rs.getInt("appointment_id"),
                        Reminder.ReminderType.valueOf(rs.getString("reminder_type")),
                        rs.getTimestamp("target_time").toLocalDateTime(),
                        rs.getString("message")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static void markAsNotified(int reminderId) {
        String sql = "UPDATE Reminders SET is_notified = 1 WHERE reminder_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reminderId);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}