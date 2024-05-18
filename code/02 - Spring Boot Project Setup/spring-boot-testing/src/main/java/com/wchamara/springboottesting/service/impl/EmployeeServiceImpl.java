package com.wchamara.springboottesting.service.impl;

import com.wchamara.springboottesting.exception.ResourceNotFoundException;
import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import com.wchamara.springboottesting.service.EmployeeService;

import java.util.List;
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

    /**
     * This method is used to retrieve all Employees from the database.
     * It calls the findAll method of the EmployeeRepository to retrieve all Employees.
     * It returns a List of all Employees.
     *
     * @return A List of all Employees.
     */
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * This method is used to retrieve an Employee by their id from the database.
     * It calls the findById method of the EmployeeRepository with the provided id.
     * It returns an Optional that contains the Employee if one was found with the provided id, or an empty Optional if no Employee was found.
     *
     * @param id The id of the Employee to retrieve.
     * @return An Optional containing the Employee if one was found, or an empty Optional if no Employee was found.
     */
    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    /**
     * This method is used to delete an Employee by their id from the database.
     * It calls the deleteById method of the EmployeeRepository with the provided id.
     *
     * @param id The id of the Employee to delete.
     */
    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }

    /**
     * This method is used to update an Employee by their id in the database.
     * It first checks if an Employee with the provided id exists in the database.
     * If an Employee with the provided id exists, it updates the Employee with the provided data and returns the updated Employee.
     * If no Employee with the provided id exists, it throws a ResourceNotFoundException.
     *
     * @param id       The id of the Employee to update.
     * @param employee The Employee data to update.
     * @return The updated Employee.
     * @throws ResourceNotFoundException If no Employee with the provided id exists.
     */
    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Optional<Employee> findEmployee = employeeRepository.findById(id);

        if (findEmployee.isPresent()) {
            Employee existingEmployee = findEmployee.get();
            if (employee.getFirstName() != null) {
                existingEmployee.setFirstName(employee.getFirstName());
            }
            if (employee.getLastName() != null) {
                existingEmployee.setLastName(employee.getLastName());
            }

            if (employee.getEmail() != null) {
                existingEmployee.setEmail(employee.getEmail());
            }

            return employeeRepository.save(existingEmployee);
        } else {
            throw new ResourceNotFoundException("Employee not found with id : " + id);
        }
    }
}