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
//      print the employees
        employees.forEach(employee1 -> System.out.println(employee1.getId() + " " + employee1.getFirstName() + " " + employee1.getLastName() + " " + employee1.getEmail()));
    }


}