/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BLL;

import DAL.AppointmentDAL;
import DTO.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class AppointmentManager {
    public static List<Appointment> getUpcomingAppointments() {
        return AppointmentDAL.getAppointments();
    }

    public static Appointment getAppointmentById(int appointmentId) {
        return AppointmentDAL.getAppointmentById(appointmentId);
    }

    public static List<Appointment> getAppointmentByDate(LocalDate appointmentDate){
        return AppointmentDAL.getAppointmentsByDate(appointmentDate);
    }

    public static HashSet<Integer> getAppointedDays(int month, int year){
        return AppointmentDAL.getAppointedDays(month,year);
    }

    public static String addAppointment(Appointment appointment) {
        if (appointment.getName() == null || appointment.getName().trim().isEmpty()) {
            return "Tên cuộc hẹn không được để trống!";
        }
        if (appointment.getStartTime().isBefore(java.time.LocalDateTime.now())) {
            return "Không thể thêm cuộc hẹn vào thời gian hoặc ngày đã qua!";
        }
        if (appointment.getStartTime().isAfter(appointment.getEndTime()) || appointment.getStartTime().isEqual(appointment.getEndTime())) {
            return "Thời gian kết thúc phải diễn ra sau thời gian bắt đầu!";
        }

        int isSuccess = AppointmentDAL.insertAppointment(appointment);
        
        if (isSuccess > 0) {
            return "SUCCESS";
        } else {
            return "Lỗi hệ thống: Không thể lưu cuộc hẹn vào cơ sở dữ liệu.";
        }
    }

    public static boolean deleteAppointment(int appointmentId) {
        return AppointmentDAL.deleteAppointment(appointmentId);
    }

    public static Appointment checkTimeConflict(LocalDateTime startTime, LocalDateTime endTime, int currentAppointmentId) {
        for (Appointment appointment : AppointmentDAL.getAppointments()) {
            if (appointment.getAppointmentId() == currentAppointmentId) {
                continue;
            }

            if (startTime.isBefore(appointment.getEndTime()) && endTime.isAfter(appointment.getStartTime())) {
                return appointment;
            }
        }
        return null;
    }

    public static boolean replaceAppointment(int appointmentId, Appointment newAppointment) {
        newAppointment.setAppointmentId(appointmentId);
        return AppointmentDAL.updateAppointment(newAppointment);
    }
}
