package src.screen;

import src.EmployeeBiometric;
import src.entity.Employee;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegisterScreen extends JFrame {

    public RegisterScreen() {
        setTitle("Register Screen");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // closes only this window

        JPanel panel = new JPanel();


        JLabel label = new JLabel("Register - Add fingerprint logic here", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label, BorderLayout.CENTER);

        List<Employee> employees = EmployeeBiometric.retrieveAllEmployee();
        JComboBox<String> dropdown = new JComboBox<>();

        for (Employee employee : employees) {
            dropdown.addItem(employee.getEmployee_id() + " - " + employee.getFirst_name() + " " +employee.getLast_name());
        }

        panel.setLayout(new FlowLayout());
        panel.add(dropdown);

        add(panel);
    }
}
