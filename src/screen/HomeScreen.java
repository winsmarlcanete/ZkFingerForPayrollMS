package src.screen;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomeScreen extends JFrame {

    public HomeScreen() {
        setTitle("SynergyGrafixCorp Biometrics");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main layout
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setLayout(new BorderLayout(20, 20));

        // Top Logo and Title
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.setOpaque(false);

        JLabel logoLabel = new JLabel("⭘⭘  Synergy", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(34, 139, 34)); // Green

        JLabel subLabel = new JLabel("GrafixCorp", SwingConstants.CENTER);
        subLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        subLabel.setForeground(Color.BLACK);

        JPanel logoWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logoWrapper.setOpaque(false);
        logoWrapper.add(logoLabel);
        logoWrapper.add(subLabel);






        JLabel timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        // Formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy hh:mm:ss a");

        // Timer to update label every 1000ms (1 second)
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(now.format(formatter));
        });
        timer.start();


        topPanel.add(logoWrapper);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(timeLabel);



        // Button Panel (2 rows)
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));



        JButton exitButton = createGreenButton("Exit");



        buttonPanel.add(exitButton);



        exitButton.addActionListener(e -> System.exit(0));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton createGreenButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(34, 139, 34)); // Forest Green
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34)));
        button.setPreferredSize(new Dimension(100, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeScreen().setVisible(true));
    }
}

