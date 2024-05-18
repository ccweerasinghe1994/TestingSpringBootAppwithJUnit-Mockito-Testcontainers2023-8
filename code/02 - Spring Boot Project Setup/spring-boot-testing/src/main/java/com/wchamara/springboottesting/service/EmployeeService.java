package com.wchamara.springboottesting.service;

import com.wchamara.springboottesting.model.Employee;

import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
}
