package VIEW;

import BLL.ReminderManager;
import DTO.Reminder;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReminderNotification {

    public ReminderNotification() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Cứ 30 giây gọi kiểm tra 1 lần, vừa nhẹ máy vừa chính xác
        scheduler.scheduleAtFixedRate(() -> {

            // GỌI HÀM MỚI LÀM: Chỉ lấy những đứa đã đến giờ và chưa thông báo
            List<Reminder> dueReminders = ReminderManager.getDueReminders();

            for (Reminder reminder : dueReminders) {

                String title = "Thông báo lịch hẹn";
                switch (reminder.getReminderType()) {
                    case NOW: title = "Đến giờ bắt đầu!"; break;
                    case M10: title = "Còn 10 phút nữa"; break;
                    case M30: title = "Còn 30 phút nữa"; break;
                    case H1:  title = "Còn 1 giờ nữa"; break;
                    case H12: title = "Còn 12 giờ nữa"; break;
                    case H24: title = "Còn 24 giờ nữa"; break;
                }

                final String finalTitle = title;
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null,
                            "🔔 NHẮC NHỞ: " + reminder.getMessage(),
                            finalTitle,
                            JOptionPane.INFORMATION_MESSAGE);
                });

                // Đánh dấu đã báo xong để lần kiểm tra sau (30 giây nữa) nó không hiện lại
                ReminderManager.markAsNotified(reminder.getReminderId());
            }

        }, 0, 30, TimeUnit.SECONDS);
    }
}