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

![alt text](image-7.png)

## 006 Build GetAllEmployees REST API

```java
package com.wchamara.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * This class is a test class for the EmployeeController.
 * It uses the Spring Boot Test framework to provide an environment for testing the controller.
 * It mocks the EmployeeService to isolate the controller for unit testing.
 * It uses the MockMvc to simulate HTTP requests to the controller.
 * It uses the ObjectMapper to convert the Employee objects to JSON format for the HTTP requests.
 */
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    /**
     * This method sets up the test environment before each test.
     * It creates a new Employee object that is used in the tests.
     */
    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .email("new@gmail.com")
                .build();
    }

    /**
     * This test verifies that the createEmployee method of the EmployeeController works as expected.
     * It sets up the EmployeeService to return the Employee that is passed to it.
     * It then sends a POST request to the /api/v1/employees endpoint with the Employee as the body.
     * It verifies that the HTTP status is 201 Created and that the returned Employee has the same properties as the one that was sent.
     */
    @Test
    @DisplayName("Employee creation succeeds")
    void givenEmployee_whenCreateEmployee_thenEmployeeIsCreated() throws Exception {
        // given - precondition or setup
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when action or the behaviour we are going to test
        ResultActions response = mockMvc.perform(
                post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee))
        );

        // then verify the output
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail()))).andDo(
                        MockMvcResultHandlers.print()
                );
    }
}
```

The selected code is a unit test class for the `EmployeeController` in a Spring Boot application. The class is annotated with `@WebMvcTest(EmployeeController.class)`, which is a special test annotation provided by Spring Boot for testing MVC controllers. This annotation auto-configures the Spring MVC infrastructure for the test and restricts the application context to the controller under test.

```java
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
```

The class has three fields that are automatically injected by Spring Boot: `MockMvc`, `EmployeeService`, and `ObjectMapper`. `MockMvc` is used to send HTTP requests to the controller under test. `EmployeeService` is a mock of the service that the controller depends on, and `ObjectMapper` is used to convert objects to JSON format for the HTTP requests.

```java
@Autowired
MockMvc mockMvc;

@MockBean
private EmployeeService employeeService;

@Autowired
private ObjectMapper objectMapper;
```

The `setUp` method is annotated with `@BeforeEach`, which means it is run before each test. This method initializes an `Employee` object that is used in the tests.

```java
@BeforeEach
void setUp() {
    employee = Employee.builder()
            .firstName("NewFirstName")
            .lastName("NewLastName")
            .email("new@gmail.com")
            .build();
}
```

The `givenEmployee_whenCreateEmployee_thenEmployeeIsCreated` test verifies the `createEmployee` method of the `EmployeeController`. It sets up the `EmployeeService` to return the `Employee` that is passed to it. It then sends a POST request to the `/api/v1/employees` endpoint with the `Employee` as the body. It verifies that the HTTP status is 201 Created and that the returned `Employee` has the same properties as the one that was sent.

```java
@Test
@DisplayName("Employee creation succeeds")
void givenEmployee_whenCreateEmployee_thenEmployeeIsCreated() throws Exception {
    // given - precondition or setup
    BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

    // when action or the behaviour we are going to test
    ResultActions response = mockMvc.perform(
            post("/api/v1/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(employee))
    );

    // then verify the output
    response.andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail()))).andDo(
                    MockMvcResultHandlers.print()
            );
}
```

This test follows the Arrange-Act-Assert pattern, which is a common pattern for structuring unit tests. The "Arrange" part sets up the test, the "Act" part performs the action that is being tested, and the "Assert" part verifies the result.

## 007 Unit test GetAllEmployees REST API

```java
package com.wchamara.springboottesting.controller;

import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
}

```

```java
package com.wchamara.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.service.EmployeeService;
import com.wchamara.springboottesting.util.FileUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * This class is a test class for the EmployeeController.
 * It uses the Spring Boot Test framework to provide an environment for testing the controller.
 * It mocks the EmployeeService to isolate the controller for unit testing.
 * It uses the MockMvc to simulate HTTP requests to the controller.
 * It uses the ObjectMapper to convert the Employee objects to JSON format for the HTTP requests.
 */
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    /**
     * This method sets up the test environment before each test.
     * It creates a new Employee object that is used in the tests.
     */
    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .email("new@gmail.com")
                .build();
    }

    /**
     * This test verifies that the createEmployee method of the EmployeeController works as expected.
     * It sets up the EmployeeService to return the Employee that is passed to it.
     * It then sends a POST request to the /api/v1/employees endpoint with the Employee as the body.
     * It verifies that the HTTP status is 201 Created and that the returned Employee has the same properties as the one that was sent.
     */
    @Test
    @DisplayName("Employee creation succeeds")
    void givenEmployee_whenCreateEmployee_thenEmployeeIsCreated() throws Exception {
        // given - precondition or setup
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when action or the behaviour we are going to test
        ResultActions response = mockMvc.perform(
                post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee))
        );

        // then verify the output
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail()))).andDo(
                        MockMvcResultHandlers.print()
                );
    }

    @Test
    @DisplayName("get all Employees")
    void givenEmployees_whenGetAllEmployees_thenReturnJsonArray() throws Exception {
        // given
        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        // when
        ResultActions response = mockMvc.perform(
                get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", CoreMatchers.is(employee.getEmail()))).andDo(
                        MockMvcResultHandlers.print()
                );
    }

    @Test
    @DisplayName("test")
    void givenEmployee() throws Exception {
        String fileName = "sample-data/user.json";
        FileUtil fileUtil = new FileUtil();
        List<Employee> employees = fileUtil.readEmployees(fileName);
//        print the employees
        employees.forEach(employee1 -> System.out.println(employee1.getId() + " " + employee1.getFirstName() + " " + employee1.getLastName() + " " + employee1.getEmail()));
    }


}
```

## 008 Refactoring JUnit test to use static imports

```java
package com.wchamara.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.service.EmployeeService;
import com.wchamara.springboottesting.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class is a test class for the EmployeeController.
 * It uses the Spring Boot Test framework to provide an environment for testing the controller.
 * It mocks the EmployeeService to isolate the controller for unit testing.
 * It uses the MockMvc to simulate HTTP requests to the controller.
 * It uses the ObjectMapper to convert the Employee objects to JSON format for the HTTP requests.
 */
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    /**
     * This method sets up the test environment before each test.
     * It creates a new Employee object that is used in the tests.
     */
    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .email("new@gmail.com")
                .build();
    }
 
    /**
     * This test verifies that the createEmployee method of the EmployeeController works as expected.
     * It sets up the EmployeeService to return the Employee that is passed to it.
     * It then sends a POST request to the /api/v1/employees endpoint with the Employee as the body.
     * It verifies that the HTTP status is 201 Created and that the returned Employee has the same properties as the one that was sent.
     */
    @Test
    @DisplayName("Employee creation succeeds")
    void givenEmployee_whenCreateEmployee_thenEmployeeIsCreated() throws Exception {
        // given - precondition or setup
        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when action or the behaviour we are going to test
        ResultActions response = mockMvc.perform(
                post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee))
        );

        // then verify the output
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail()))).andDo(
                        print()
                );
    }

    @Test
    @DisplayName("get all Employees")
    void givenEmployees_whenGetAllEmployees_thenReturnJsonArray() throws Exception {
        // given
        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        // when
        ResultActions response = mockMvc.perform(
                get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$[0].email", is(employee.getEmail()))).andDo(
                        print()
                );
    }

    @Test
    @DisplayName("test")
    void givenEmployee() throws Exception {
        String fileName = "sample-data/user.json";

        List<Employee> employees = FileUtil.readEmployees(fileName);
//      print the employees
        employees.forEach(employee1 -> System.out.println(employee1.getId() + " " + employee1.getFirstName() + " " + employee1.getLastName() + " " + employee1.getEmail()));
    }


}
```

## 009 Build getEmployeeById REST API

```java
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long employeeId) {
        return employeeService.getEmployeeById(employeeId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
```

## 010 Unit test getEmployeeById REST API - Positive Scenario

```java

    @Test
    @DisplayName("Given valid ID, then return Employee")
    void givenValidId_thenReturnEmployee() throws Exception {
        long id = 1L;
        Employee employee = new Employee(id, "John", "Doe", "john.doe@gmail.com");
        given(employeeService.getEmployeeById(ArgumentMatchers.anyLong())).willReturn(Optional.of(employee));

        ResultActions response = mockMvc.perform(
                get("/api/v1/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }
```

## 011 Unit test getEmployeeById REST API - Negative Scenario

```java
    @Test
    @DisplayName("Given invalid ID, then return not found")
    void givenInvalidId_thenReturnNotFound() throws Exception {
        given(employeeService.getEmployeeById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(
                get("/api/v1/employees/999")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isNotFound()).andDo(print());
    }

```

## 012 Build updateEmployee REST API

```java
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody Employee employee) {
        return employeeService.getEmployeeById(employeeId)
                .map(employeeObj -> {
                    return ResponseEntity.ok(employeeService.updateEmployee(employeeId, employee));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

```

## 013 Unit test updateEmployee REST API - Positive Scenario

```java
    @Test
    @DisplayName("Given valid ID and Employee, then return updated Employee")
    void givenValidIdAndEmployee_thenReturnUpdatedEmployee() throws Exception {
        Employee updated = new Employee(1L, "UpdatedFirstName", "UpdatedLastName", "updated@gmail.com");
        given(employeeService.getEmployeeById(ArgumentMatchers.anyLong())).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Employee.class))).willReturn(updated);

        ResultActions response = mockMvc.perform(
                put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updated.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updated.getLastName())))
                .andExpect(jsonPath("$.email", is(updated.getEmail())));
    }
```

## 014 Unit test updateEmployee REST API - Negative Scenario

```java
    @Test
    @DisplayName("Given invalid ID, then return not found")
    void givenInvalidEmployeeId_thenReturnNotFound() throws Exception {
        long wrongId = 999L;
        Employee employee = new Employee(1L, "UpdatedFirstName", "UpdatedLastName", "updated@gmail.com");
        given(employeeService.getEmployeeById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(
                put("/api/v1/employees/{id}", wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee))
        );

        response.andExpect(status().isNotFound()).andDo(print());
    }
```

## 015 Build deleteEmployee REST API

```java

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long employeeId) {
        return employeeService.getEmployeeById(employeeId)
                .map(employee -> {
                    employeeService.deleteEmployeeById(employeeId);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
```

## 016 Unit test deleteEmployee REST API

```java
    @Test
    @DisplayName("Given valid ID, then delete Employee")
    void givenValidId_thenDeleteEmployee() throws Exception {
        Employee employee = new Employee(1L, "John", "Doe", "john.doe@gmail.com");
        given(employeeService.getEmployeeById(ArgumentMatchers.anyLong())).willReturn(Optional.of(employee));

        ResultActions response = mockMvc.perform(
                delete("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk());
        verify(employeeService, times(1)).deleteEmployeeById(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("Given invalid ID, then return not found")
    void givenInvalidId_whenDeleteEmployee_thenReturnNotFound() throws Exception {
        given(employeeService.getEmployeeById(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(
                delete("/api/v1/employees/999")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isNotFound());
        verify(employeeService, times(0)).deleteEmployeeById(ArgumentMatchers.anyLong());
    }
```
