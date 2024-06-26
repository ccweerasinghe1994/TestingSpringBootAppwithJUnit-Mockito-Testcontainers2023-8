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
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

}