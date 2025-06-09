package src;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import src.entity.Employee;
public class EmployeeBiometric {
    public static List<Employee> retrieveAllEmployee(){
        List<Employee> employees = new ArrayList<>();
        Connection conn;

        try{
            String sql = "SELECT `employees`.`employee_id`,\n" +
                    "    `employees`.`last_name`,\n" +
                    "    `employees`.`first_name`,\n" +
                    "    `employees`.`pay_rate`,\n" +
                    "    `employees`.`department`\n" +
                    "FROM `payrollmsdb`.`employees`;";
            conn = JDBC.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                BigDecimal payRate = rs.getBigDecimal("pay_rate");
                String department = rs.getString("department");

                Employee emp = new Employee(employeeId, lastName, firstName, payRate, department);
                employees.add(emp);
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }
}
