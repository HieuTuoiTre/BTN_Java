package VIEW;


import DTO.Appointment;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;


public class AppointmentDetail extends JDialog {
    private Appointment currentApt;
    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_BG = Color.WHITE;

    public AppointmentDetail(Window parent, boolean modal, Appointment apt) {
        super(parent, modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
        this.currentApt = apt;

        setTitle("Chi tiết lịch hẹn");
        setSize(280, 350);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // --- HEADER ---
        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel lblTitle = new JLabel(currentApt.getName());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblTitle);
        panelHeader.add(Box.createVerticalStrut(5));
        add(panelHeader, BorderLayout.NORTH);

        // --- CENTER INFO ---
        JPanel panelCenter = new JPanel(new BorderLayout());
        panelCenter.setBackground(COLOR_BG);
        panelCenter.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel panelInfo = new JPanel(new GridLayout(3, 1, 0, 10));
        panelInfo.setBackground(COLOR_BG);
        panelInfo.setBorder(new EmptyBorder(0, 0, 20, 0));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");
        JLabel lblStart = new JLabel("Bắt đầu: " + currentApt.getStartTime().format(dtf));
        JLabel lblEnd = new JLabel("Kết thúc: " + currentApt.getEndTime().format(dtf));

        Font fontInfo = new Font("Segoe UI", Font.PLAIN, 16);
        lblStart.setFont(fontInfo); lblEnd.setFont(fontInfo);

        panelInfo.add(lblStart); panelInfo.add(lblEnd);
        panelCenter.add(panelInfo, BorderLayout.NORTH);

        add(panelCenter, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelFooter.setBackground(COLOR_BG);
        panelFooter.setBorder(new EmptyBorder(10, 0, 20, 0));

        RoundedButton btnClose = new RoundedButton("Đóng", 15, COLOR_PRIMARY, 1);
        btnClose.setBackground(COLOR_PRIMARY);
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnClose.setPreferredSize(new Dimension(120, 40));
        btnClose.addActionListener(e -> dispose());

        panelFooter.add(btnClose);
        add(panelFooter, BorderLayout.SOUTH);
    }
}