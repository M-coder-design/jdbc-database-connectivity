package org.example;

import org.example.model.Employee;
import org.example.service.EmployeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create Spring context
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("org.example");

        // Get EmployeeService bean
        EmployeeService employeeService = context.getBean(EmployeeService.class);

        // Create a new employee
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("Hansaraj");
        newEmployee.setLastName("Doe");
        newEmployee.setEmail("hansaraj@e-arc.com");
        newEmployee.setSalary(new BigDecimal("50000.00"));

        // Save employee
        Employee savedEmployee = employeeService.createEmployee(newEmployee);
        System.out.println("Saved employee with ID: " + savedEmployee.getId());

        Employee newEmployee3 = new Employee();
        newEmployee3.setFirstName("Emily");
        newEmployee3.setLastName("Chen");
        newEmployee3.setEmail("emily.chen@e-arc.com");
        newEmployee3.setSalary(new BigDecimal("55000.00"));
        Employee savedEmployee3 = employeeService.createEmployee(newEmployee3);
        System.out.println("Saved employee with ID: " + savedEmployee3.getId());

        Employee newEmployee4 = new Employee();
        newEmployee4.setFirstName("David");
        newEmployee4.setLastName("Lee");
        newEmployee4.setEmail("david.lee@e-arc.com");
        newEmployee4.setSalary(new BigDecimal("70000.00"));
        Employee savedEmployee4 = employeeService.createEmployee(newEmployee4);
        System.out.println("Saved employee with ID: " + savedEmployee4.getId());

        Employee newEmployee5 = new Employee();
        newEmployee5.setFirstName("Sophia");
        newEmployee5.setLastName("Patel");
        newEmployee5.setEmail("sophia.patel@e-arc.com");
        newEmployee5.setSalary(new BigDecimal("58000.00"));
        Employee savedEmployee5 = employeeService.createEmployee(newEmployee5);
        System.out.println("Saved employee with ID: " + savedEmployee5.getId());

        Employee newEmployee6 = new Employee();
        newEmployee6.setFirstName("Oliver");
        newEmployee6.setLastName("Brown");
        newEmployee6.setEmail("oliver.brown@e-arc.com");
        newEmployee6.setSalary(new BigDecimal("62000.00"));
        Employee savedEmployee6 = employeeService.createEmployee(newEmployee6);
        System.out.println("Saved employee with ID: " + savedEmployee6.getId());

        // Get all employees
        List<Employee> allEmployees = employeeService.getAllEmployees();
        System.out.println("Total employees: " + allEmployees.size());

        // Print all employees
        for (Employee employee : allEmployees) {
            System.out.println("Employee ID: " + employee.getId());
            System.out.println("Name: " + employee.getFirstName() + " " + employee.getLastName());
            System.out.println("Email: " + employee.getEmail());
            System.out.println("Salary: " + employee.getSalary());
            System.out.println(); // Empty line for better readability
        }


        // Give everyone a 10% raise
        employeeService.giveRaiseToAll(new BigDecimal("10"));

        //deleting all employees
        employeeService.deleteAllEmployees();

        // Close the context
        context.close();
    }
}