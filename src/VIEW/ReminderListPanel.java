package VIEW;

import BLL.AppointmentManager;
import BLL.ReminderManager;
import DTO.Appointment;
import DTO.Reminder;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReminderListPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Reminder> currentList;


    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_DANGER = new Color(220, 53, 69);

    public ReminderListPanel() {
        initComponents();
        refreshReminders();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("Trung tâm Thông báo (24h tới)", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // 2. Cấu hình Bảng (Table)
        String[] columns = {"ID", "Tên lịch hẹn", "Nội dung nhắc nhở", "Loại thông báo", "Thời điểm báo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        // Căn giữa Header
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Căn giữa nội dung các ô
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Kiểu dáng bảng hiện đại
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(190, 220, 255));
        table.setSelectionForeground(new Color(33, 37, 41));
        table.setShowVerticalLines(false);

        // Ẩn cột ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        add(scrollPane, BorderLayout.CENTER);

        // 3. Footer với các nút căn giữa (Giống AppointmentListPanel)
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        panelFooter.setBackground(Color.WHITE);

        RoundedButton btnEdit = new RoundedButton("Chỉnh Sửa", 15, COLOR_PRIMARY, 1);
        btnEdit.setBackground(COLOR_PRIMARY);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setPreferredSize(new Dimension(160, 45));

        RoundedButton btnDelete = new RoundedButton("Xóa Bỏ", 15, COLOR_DANGER, 1);
        btnDelete.setBackground(COLOR_DANGER);
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setPreferredSize(new Dimension(160, 45));

        panelFooter.add(btnEdit);
        panelFooter.add(btnDelete);
        add(panelFooter, BorderLayout.SOUTH);

        // Sự kiện nút bấm
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());
    }

    public void refreshReminders() {
        tableModel.setRowCount(0);
        currentList = ReminderManager.getRemindersByCalendar_24H();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");
        for (Reminder r : currentList) {
            Appointment currentApt = AppointmentManager.getAppointmentById(r.getAppointmentId());

            String typeStr = r.getReminderType().toString();
            switch (typeStr) {
                case "NOW": typeStr = "Tại lúc bắt đầu"; break;
                case "M10": typeStr = "Trước 10 phút"; break;
                case "M30": typeStr = "Trước 30 phút"; break;
                case "H1": typeStr = "Trước 1 giờ"; break;
                case "H12": typeStr = "Trước 12 giờ"; break;
                case "H24": typeStr = "Trước 1 ngày"; break;
                default: typeStr = "Không rõ"; break;
            }

            tableModel.addRow(new Object[]{
                    r.getReminderId(),
                    currentApt != null ? currentApt.getName() : "N/A",
                    r.getMessage(),
                    typeStr,
                    r.getTargetTime().format(formatter)
            });
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một thông báo để sửa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Reminder selectedReminder = currentList.get(row);
        Appointment currentApt = AppointmentManager.getAppointmentById(selectedReminder.getAppointmentId());

        if (currentApt == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy cuộc hẹn liên quan!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ReminderDialog dialog = new ReminderDialog((Frame) SwingUtilities.getWindowAncestor(this), true, currentApt, selectedReminder);
        dialog.setVisible(true);
        refreshReminders();
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một thông báo để xóa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int reminderId = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa thông báo này?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = ReminderManager.deleteReminder(reminderId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Đã xóa nhắc nhở thành công!");
                refreshReminders();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}