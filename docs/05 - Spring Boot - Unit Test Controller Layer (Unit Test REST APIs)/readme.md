# 05 - Spring Boot - Unit Test Controller Layer (Unit Test REST APIs)

## 001 Controller Layer unit testing overview

![alt text](image.png)
![alt text](image-1.png)
![alt text](image-2.png)
![alt text](image-3.png)
![alt text](image-4.png)

## 002 Overview of @WebMvcTest annotation

![alt text](image-5.png)

## 003 @WebMvcTest VS @SpringBootTest

![alt text](image-6.png)

## 004 Build createEmployee REST API

```java
package com.wchamara.springboottesting.controller;

import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }
}

```

```http
###
POST http://localhost:8080/api/v1/employees
Content-Type: application/json

{
  "firstName": "chamara",
  "lastName": "weerasinghe",
  "email": "abc@gmail.com"
}

response

{
  "id": 1,
  "firstName": "chamara",
  "lastName": "weerasinghe",
  "email": "abc@gmail.com"
}

```

## 005 Unit test createEmployee REST API

## 006 Build GetAllEmployees REST API

## 007 Unit test GetAllEmployees REST API

## 008 Refactoring JUnit test to use static imports

## 009 Build getEmployeeById REST API

## 010 Unit test getEmployeeById REST API - Positive Scenario

## 011 Unit test getEmployeeById REST API - Negative Scenario

## 012 Build updateEmployee REST API

## 013 Unit test updateEmployee REST API - Positive Scenario

## 014 Unit test updateEmployee REST API - Negative Scenario

## 015 Build deleteEmployee REST API

## 016 Unit test deleteEmployee REST API
