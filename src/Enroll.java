package src;
import src.EmployeeBiometric;
import src.entity.Employee;

import java.util.List;



public class Enroll {

    public static void EnrollEmployee(){
        List<Employee> emplist = EmployeeBiometric.retrieveAllEmployee();
        for (Employee employee : emplist){
            employee.getEmployee_id();
        }
    }
}
