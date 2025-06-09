package src;

import src.entity.Employee;
import src.screen.HomeScreen;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<Employee> emplist = EmployeeBiometric.retrieveAllEmployee();
        for (Employee employee : emplist) {
            System.out.println(employee.getLast_name());
        }

        SwingUtilities.invokeLater(() -> {
            new HomeScreen().setVisible(true);
        });
    }
}
