package com.wchamara.springboottesting.service;

import com.wchamara.springboottesting.model.Employee;

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
}