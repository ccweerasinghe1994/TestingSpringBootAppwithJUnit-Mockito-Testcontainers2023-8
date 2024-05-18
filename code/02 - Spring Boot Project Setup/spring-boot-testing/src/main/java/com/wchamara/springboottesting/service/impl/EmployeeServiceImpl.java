package com.wchamara.springboottesting.service.impl;

import com.wchamara.springboottesting.exception.ResourceNotFoundException;
import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import com.wchamara.springboottesting.service.EmployeeService;

import java.util.Optional;

/**
 * This class implements the EmployeeService interface.
 * It provides the business logic for managing Employees.
 */
public class EmployeeServiceImpl implements EmployeeService {

    /**
     * The repository for accessing the Employee data from the database.
     */
    private final EmployeeRepository employeeRepository;

    /**
     * Constructor for the EmployeeServiceImpl.
     * It takes an EmployeeRepository as a parameter and assigns it to the employeeRepository field.
     *
     * @param employeeRepository The repository for accessing the Employee data from the database.
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * This method is used to save an Employee to the database.
     * It first checks if an Employee with the same id already exists in the database.
     * If an Employee with the same id already exists, it throws a ResourceNotFoundException.
     * If no Employee with the same id exists, it saves the Employee to the database and returns the saved Employee.
     *
     * @param employee The Employee to save.
     * @return The saved Employee.
     * @throws ResourceNotFoundException If an Employee with the same id already exists.
     */
    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> findEmployee = employeeRepository.findById(employee.getId());

        if (findEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exists with given email : " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }
}