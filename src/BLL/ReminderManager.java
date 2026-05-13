/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BLL;


import DAL.ReminderDAL;
import DTO.Reminder;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ReminderManager {
    public static List<Reminder> getRemindersByCalendar_24H() {
        return ReminderDAL.getRemindersByCalendar_24H();
    }

    public static String addReminder(Reminder reminder) {
        if (reminder.getAppointmentId() <= 0) {
            return "Lỗi: Không xác định được Cuộc hẹn để tạo nhắc nhở!";
        }

        if (reminder.getTargetTime() == null) {
            return "Lỗi: Thời gian nhắc nhở không được để trống!";
        }

        if (reminder.getTargetTime().isBefore(LocalDateTime.now())) {
            return "Thời gian nhắc nhở không hợp lệ vì đã trôi qua!";
        }

        boolean isSuccess = ReminderDAL.insertReminder(reminder);

        if (isSuccess) {
            return "SUCCESS";
        } else {
            return "Lỗi hệ thống: Không thể lưu nhắc nhở vào cơ sở dữ liệu.";
        }
    }

    public static String updateReminder(Reminder reminder) {
        if (reminder.getTargetTime().isBefore(LocalDateTime.now())) {
            return "Thời gian nhắc nhở không hợp lệ vì đã trôi qua!";
        }
        boolean success = ReminderDAL.updateReminder(reminder);
        return success ? "SUCCESS" : "Lỗi hệ thống khi cập nhật nhắc nhở.";
    }

    public static boolean deleteReminder(int reminderId) {
        return DAL.ReminderDAL.deleteReminder(reminderId);
    }

    public static boolean deleteRemindersByAppointmentId(int appointmentId) {
        return ReminderDAL.deleteRemindersByAppointmentId(appointmentId);
    }
    public static List<Reminder> getDueReminders(){
        return ReminderDAL.getDueReminders();
    }
    public static void markAsNotified(int reminderId){
        ReminderDAL.markAsNotified(reminderId);
    }
}
