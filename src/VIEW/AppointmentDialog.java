package VIEW;

import BLL.AppointmentManager;
import BLL.ReminderManager;
import DTO.Appointment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentDialog extends JDialog {
    private JTextField txtName;
    private JSpinner spinStartHour;
    private JSpinner spinStartMinute;
    private JSpinner spinEndHour;
    private JSpinner spinEndMinute;
    private RoundedButton btnSave;
    private RoundedButton btnCancel;

    private LocalDate selectedDate;
    private Appointment appointmentToEdit;

    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_TEXT_DARK = new Color(50, 50, 50);
    private final Color COLOR_BORDER = new Color(210, 215, 220);

    public AppointmentDialog(Frame parent, boolean modal, LocalDate date, Appointment aptToEdit) {
        super(parent, modal);
        this.appointmentToEdit = aptToEdit;

        this.selectedDate = aptToEdit != null ? aptToEdit.getStartTime().toLocalDate() : date;

        setTitle(aptToEdit == null ? "Thêm Lịch Hẹn" : "Chỉnh Sửa Lịch Hẹn");
        setSize(480, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());

        initComponents();

        if (aptToEdit != null) {
            fillDataForEdit();
        }
    }

    private void initComponents() {
        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(25, 0, 25, 0));

        JLabel lblTitle = new JLabel("THÊM LỊCH HẸN MỚI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        JLabel lblDate = new JLabel("Ngày: " + selectedDate.format(formatter));
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblDate.setForeground(new Color(220, 235, 255));
        lblDate.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblTitle);
        panelHeader.add(Box.createVerticalStrut(8));
        panelHeader.add(lblDate);

        add(panelHeader, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_BG);
        panelForm.setBorder(new EmptyBorder(25, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 12, 15);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        JLabel lblName = new JLabel("Tên lịch hẹn:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblName, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.65;
        txtName = new JTextField();
        styleTextField(txtName);
        panelForm.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblStart = new JLabel("Giờ bắt đầu:");
        lblStart.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStart.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblStart, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JPanel panelStartTime = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelStartTime.setBackground(COLOR_BG);
        spinStartHour = new JSpinner(new SpinnerNumberModel(8, 0, 23, 1));
        spinStartMinute = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
        styleSpinner(spinStartHour);
        styleSpinner(spinStartMinute);
        panelStartTime.add(spinStartHour);
        JLabel sep1 = new JLabel(" : ");
        sep1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelStartTime.add(sep1);
        panelStartTime.add(spinStartMinute);
        panelForm.add(panelStartTime, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblEnd = new JLabel("Giờ kết thúc:");
        lblEnd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEnd.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblEnd, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        JPanel panelEndTime = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelEndTime.setBackground(COLOR_BG);
        spinEndHour = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
        spinEndMinute = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
        styleSpinner(spinEndHour);
        styleSpinner(spinEndMinute);
        panelEndTime.add(spinEndHour);
        JLabel sep2 = new JLabel(" : ");
        sep2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelEndTime.add(sep2);
        panelEndTime.add(spinEndMinute);
        panelForm.add(panelEndTime, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panelFooter.setBackground(COLOR_BG);
        panelFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER));

        btnCancel = new RoundedButton("Hủy bỏ", 12, COLOR_BORDER, 1);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setForeground(new Color(100, 100, 100));
        btnCancel.setPreferredSize(new Dimension(120, 45));

        btnSave = new RoundedButton("Lưu Lịch Hẹn", 12, COLOR_PRIMARY, 1);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(COLOR_PRIMARY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(150, 45));

        panelFooter.add(btnCancel);
        panelFooter.add(btnSave);

        add(panelFooter, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> handleSave());
    }

    private void styleTextField(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setPreferredSize(new Dimension(200, 40));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setPreferredSize(new Dimension(75, 28));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor defaultEditor) {
            JTextField txt = defaultEditor.getTextField();
            txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            txt.setHorizontalAlignment(SwingConstants.CENTER);
            txt.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    private void fillDataForEdit() {
        txtName.setText(appointmentToEdit.getName());
        spinStartHour.setValue(appointmentToEdit.getStartTime().getHour());
        spinStartMinute.setValue(appointmentToEdit.getStartTime().getMinute());
        spinEndHour.setValue(appointmentToEdit.getEndTime().getHour());
        spinEndMinute.setValue(appointmentToEdit.getEndTime().getMinute());

        btnSave.setText("Cập nhật");
    }

    private void handleSave() {
        String name = txtName.getText().trim();
        int startH = (int) spinStartHour.getValue();
        int startM = (int) spinStartMinute.getValue();
        int endH = (int) spinEndHour.getValue();
        int endM = (int) spinEndMinute.getValue();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Tên", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime startTime = selectedDate.atTime(startH, startM);
        LocalDateTime endTime = selectedDate.atTime(endH, endM);

        if (startTime.isBefore(LocalDateTime.now())) {
            JOptionPane.showMessageDialog(this, "Không thể thêm lịch hẹn vào thời gian hoặc ngày đã qua!", "Lỗi thời gian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!startTime.isBefore(endTime)) {
            JOptionPane.showMessageDialog(this, "Giờ kết thúc phải diễn ra sau giờ bắt đầu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }


        // 2. KIỂM TRA TRÙNG LỊCH (GHI ĐÈ)
        int currentId = (appointmentToEdit != null) ? appointmentToEdit.getAppointmentId() : -1;
        Appointment conflictAppt = BLL.AppointmentManager.checkTimeConflict(startTime, endTime, currentId);

        if (conflictAppt != null) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Bạn đã có lịch hẹn: [" + conflictAppt.getName() + "] trong khung giờ này!\n\nBạn muốn GHI ĐÈ (thay thế) lịch hẹn cũ bằng lịch hẹn này không?\nChọn 'No' để ở lại và chọn khung giờ khác.",
                    "Trùng Lịch - Cảnh báo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                Appointment tempAppt = new Appointment();
                tempAppt.setName(name);
                tempAppt.setStartTime(startTime);
                tempAppt.setEndTime(endTime);


                boolean replaced = BLL.AppointmentManager.replaceAppointment(conflictAppt.getAppointmentId(), tempAppt);
                if (replaced) {
                    ReminderManager.deleteRemindersByAppointmentId(conflictAppt.getAppointmentId());
                    ReminderDialog dialog = new ReminderDialog(AppointmentDialog.this, true, tempAppt, null);
                    dialog.setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi: Không thể ghi đè lịch hẹn.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }

        // 3. NẾU KHÔNG CÓ GÌ BẤT THƯỜNG -> TẠO MỚI
        if (appointmentToEdit != null) {
            appointmentToEdit.setName(name);
            appointmentToEdit.setStartTime(startTime);
            appointmentToEdit.setEndTime(endTime);

            boolean success = BLL.AppointmentManager.replaceAppointment(appointmentToEdit.getAppointmentId(), appointmentToEdit);

            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật lịch hẹn thành công!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Không thể cập nhật lịch hẹn.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            Appointment newAppointment = new Appointment();
            newAppointment.setName(name);
            newAppointment.setStartTime(startTime);
            newAppointment.setEndTime(endTime);

            String result = BLL.AppointmentManager.addAppointment(newAppointment);

            if (result.equals("SUCCESS")) {
                ReminderDialog dialog = new ReminderDialog(AppointmentDialog.this, true, newAppointment, null);
                dialog.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, result, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}