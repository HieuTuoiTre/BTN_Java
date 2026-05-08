package VIEW;

import BLL.AppointmentManager;
import DTO.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentListPanel extends JPanel {
    private RoundedButton btnView;
    private final RoundedButton btnEdit;
    private final RoundedButton btnDelete;
    private final RoundedButton btnAddReminder;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Appointment> currentList;

    public AppointmentListPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Danh sách Cuộc hẹn Sắp tới", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 86, 179));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Khởi tạo Bảng (Table)
        String[] columns = {"ID", "Tên cuộc hẹn", "Bắt đầu", "Kết thúc", "Phân loại"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Không cho sửa trực tiếp trên ô
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(190, 220, 255));
        table.setSelectionForeground(new Color(33, 37, 41));
        table.setShowVerticalLines(false);

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        panelFooter.setBackground(Color.WHITE);

        btnView = new RoundedButton("Xem Chi Tiết", 15, new Color(23, 162, 184), 1);
        btnView.setBackground(new Color(23, 162, 184));
        btnView.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnView.setForeground(Color.WHITE);
        btnView.setPreferredSize(new Dimension(130, 45));

        btnAddReminder = new RoundedButton("+ Thêm Nhắc Nhở", 15, new Color(40, 167, 69), 1);
        btnAddReminder.setBackground(new Color(40, 167, 69));
        btnAddReminder.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAddReminder.setForeground(Color.WHITE);
        btnAddReminder.setPreferredSize(new Dimension(180, 45));

        btnEdit = new RoundedButton("Chỉnh Sửa", 15, new Color(0, 86, 179), 1);
        btnEdit.setBackground(new Color(0, 86, 179)); // THÊM DÒNG NÀY ĐỂ TÔ MÀU NỀN
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setPreferredSize(new Dimension(160, 45));

        btnDelete = new RoundedButton("Xóa Bỏ", 15, new Color(220, 53, 69), 1);
        btnDelete.setBackground(new Color(220, 53, 69)); // THÊM DÒNG NÀY ĐỂ TÔ MÀU NỀN
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setPreferredSize(new Dimension(160, 45));

        panelFooter.add(btnView);
        panelFooter.add(btnAddReminder);
        panelFooter.add(btnEdit);
        panelFooter.add(btnDelete);

        add(panelFooter, BorderLayout.SOUTH);

        btnView.addActionListener(e -> handleViewDetails());
        btnAddReminder.addActionListener(e -> handleAddReminder());
        btnDelete.addActionListener(e -> handleDelete());
        btnEdit.addActionListener(e -> handleEdit());

        loadData();
    }

    public void loadData() {
        tableModel.setRowCount(0);
        currentList = AppointmentManager.getUpcomingAppointments();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        for (Appointment apt : currentList) {
            Object[] row = {
                    apt.getAppointmentId(), apt.getName(),
                    apt.getStartTime().format(formatter), apt.getEndTime().format(formatter),
            };
            tableModel.addRow(row);
        }
    }

    private void handleViewDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuộc hẹn để xem chi tiết!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }

    }

    private void handleAddReminder() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuộc hẹn để thêm nhắc nhở!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Appointment selectedApt = currentList.get(selectedRow);
        ReminderDialog dialog = new ReminderDialog((Frame) SwingUtilities.getWindowAncestor(this), true, selectedApt, null);
        dialog.setVisible(true);
        loadData();
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuộc hẹn để xóa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Appointment selectedApt = currentList.get(selectedRow);
        int aptId = selectedApt.getAppointmentId();
        String aptName = selectedApt.getName();


        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa cuộc hẹn: '" + aptName + "'?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (AppointmentManager.deleteAppointment(aptId)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            }
        }
    }

    private void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuộc hẹn để sửa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Appointment selectedApt = currentList.get(selectedRow);

        AppointmentDialog editForm = new AppointmentDialog((Frame) SwingUtilities.getWindowAncestor(this), true, null, selectedApt);
        editForm.setVisible(true);
        loadData();
    }
}