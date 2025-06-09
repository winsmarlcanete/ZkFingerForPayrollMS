package src.screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class HomeScreen extends JFrame {

    public HomeScreen() {
        setTitle("Payroll Management System - Biometrics Module");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Biometrics Fingerprint Scanner", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title);

        JButton enrollButton = new JButton("Enroll Fingerprint");
        JButton verifyButton = new JButton("Verify Fingerprint");
        JButton exitButton = new JButton("Exit");

        panel.add(enrollButton);
        panel.add(verifyButton);
        panel.add(exitButton);

        enrollButton.addActionListener(e -> {
            EnrollScreen enrollScreen = new EnrollScreen();
            enrollScreen.setVisible(true);});

        verifyButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "Verify function triggered."));
        exitButton.addActionListener(e -> System.exit(0));

        add(panel);
    }
}
