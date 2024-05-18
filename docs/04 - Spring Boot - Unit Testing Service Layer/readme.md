# 04 - Spring Boot - Unit Testing Service Layer

## 001 Service Layer unit testing overview

![alt text](image.png)
![alt text](image-1.png)
![alt text](image-2.png)

## 002 Create EmployeeService with saveEmployee method

```java
package com.wchamara.springboottesting.service.impl;

import com.wchamara.springboottesting.exception.ResourceNotFoundException;
import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import com.wchamara.springboottesting.service.EmployeeService;

import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> findEmployee = employeeRepository.findById(employee.getId());

        if (findEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exists with given email : " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }
}

```

```java
package com.wchamara.springboottesting.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

```java
package com.wchamara.springboottesting.service;

import com.wchamara.springboottesting.model.Employee;

import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
}
```

## 003 Quick Recap of Mockito basics (before writing JUnit tests to use Mock objects)

## 004 Unit test for EmployeeService saveEmployee method

## 005 Using @Mock and @InjectMocks annotations to mock the object

## 006 Unit test for saveEmployee method which throws Exception

## 007 Unit test for EmployeeService getAllEmployees method - Positive Scenario

## 008 Unit test for EmployeeService getAllEmployees method - Negative Scenario

## 009 Unit test for  EmployeeService getEmployeeById method

## 010 Unit test for EmployeeService updateEmployee method

## 011 Unit test for EmployeeService deleteEmployee method
