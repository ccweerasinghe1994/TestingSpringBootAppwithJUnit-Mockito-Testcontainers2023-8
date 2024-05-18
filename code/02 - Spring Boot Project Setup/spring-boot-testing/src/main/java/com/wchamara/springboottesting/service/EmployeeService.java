package com.wchamara.springboottesting.service;

import com.wchamara.springboottesting.model.Employee;

import java.util.List;
import java.util.Optional;

/**
 * This interface defines the service layer for managing Employees.
 * It declares a method for saving an Employee.
 * The implementation of this interface should provide the business logic for saving an Employee.
 */
public interface EmployeeService {

    /**
     * This method is used to save an Employee.
     * It takes an Employee as a parameter and returns the saved Employee.
     * The implementation of this method should handle the business logic for saving the Employee, such as checking if an Employee with the same id already exists.
     *
     * @param employee The Employee to save.
     * @return The saved Employee.
     */
    Employee saveEmployee(Employee employee);

    /**
     * This method is used to retrieve all Employees from the database.
     * The implementation of this method should handle the business logic for retrieving all Employees.
     *
     * @return A List of all Employees.
     */
    List<Employee> getAllEmployees();

    /**
     * This method is used to retrieve an Employee by id.
     * It takes the id of the Employee as a parameter and returns an Optional of the Employee.
     * The implementation of this method should handle the business logic for retrieving an Employee by id.
     *
     * @param id The id of the Employee to retrieve.
     * @return An Optional of the Employee.
     */
    Optional<Employee> getEmployeeById(Long id);

    /**
     * This method is used to delete an Employee by id.
     * It takes the id of the Employee as a parameter and returns void.
     * The implementation of this method should handle the business logic for deleting an Employee by id.
     *
     * @param id The id of the Employee to delete.
     */
    void deleteEmployeeById(Long id);

    /**
     * This method is used to update an Employee.
     * It takes the id of the Employee and the updated Employee as parameters and returns the updated Employee.
     * The implementation of this method should handle the business logic for updating an Employee.
     *
     * @param id       The id of the Employee to update.
     * @param employee The updated Employee.
     * @return The updated Employee.
     */
    Employee updateEmployee(Long id, Employee employee);
}