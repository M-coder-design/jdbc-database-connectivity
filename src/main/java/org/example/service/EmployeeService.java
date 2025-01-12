package org.example.service;

import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        // Additional business logic can be added here
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee employee) {
        // Verify employee exists
        getEmployeeById(id);
        employee.setId(id);
        employeeRepository.update(employee);
        return employee;
    }

    @Transactional
    public void deleteEmployee(Long id) {
        // Verify employee exists
        getEmployeeById(id);
        employeeRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllEmployees(){
        employeeRepository.deleteAll();
    }

    // Example of transaction management with multiple operations
    @Transactional
    public void giveRaiseToAll(BigDecimal percentage) {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            BigDecimal newSalary = employee.getSalary()
                    .multiply(BigDecimal.ONE.add(percentage.divide(new BigDecimal("100"))));
            employee.setSalary(newSalary);
            employeeRepository.update(employee);
        }
    }
}

