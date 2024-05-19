package com.wchamara.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import com.wchamara.springboottesting.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers(
        disabledWithoutDocker = true
)
public class EmployeeControllerIT {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeControllerIT.class);
    @Container
    private final static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"))
            .withPassword("example")
            .withUsername("root")
            .withDatabaseName("ems")
            .withLogConsumer(new Slf4jLogConsumer(logger));

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("Employee creation integration succeeds")
    void givenEmployee_whenCreateEmployee_thenEmployeeIsCreated() throws Exception {

        System.out.println("JDBC URL: " + mySQLContainer.getJdbcUrl());
        System.out.println("Username: " + mySQLContainer.getUsername());
        System.out.println("Password: " + mySQLContainer.getPassword());
        System.out.println("Database Name: " + mySQLContainer.getDatabaseName());

        Employee employee = Employee.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .email("new@gmail.com")
                .build();

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
    @DisplayName("get all employees integration")
    void givenEmployees_whenGetAllEmployees_thenReturnJsonArray() throws Exception {
        String filePath = "sample-data/user.json";
        List<Employee> employees = FileUtil.readEmployees(filePath);

        employeeRepository.saveAll(employees);

        ResultActions response = mockMvc.perform(
                get("/api/v1/employees")
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("size()", is(employees.size())))
                .andExpect(jsonPath("$[0].firstName", is(employees.get(0).getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(employees.get(0).getLastName())))
                .andExpect(jsonPath("$[0].email", is(employees.get(0).getEmail()))).andDo(
                        print()
                );
    }

    @Test
    @DisplayName("get employee by id integration")
    void givenEmployee_whenGetEmployeeById_thenReturnEmployee() throws Exception {

        Employee employee = Employee.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .email("new@gmail.com")
                .build();

        employeeRepository.save(employee);


        ResultActions response = mockMvc.perform(
                get("/api/v1/employees/{id}", employee.getId())
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail()))).andDo(
                        print()
                );
    }

    @Test
    @DisplayName("get employee by id integration fails")
    void givenEmployee_whenGetEmployeeById_thenReturnEmployeeFails() throws Exception {
        long wrongId = 1000000000000000000L;

        ResultActions response = mockMvc.perform(
                get("/api/v1/employees/{id}", wrongId)
        );

        response.andExpect(status().isNotFound()).andDo(
                print()
        );
    }

    @Test
    @DisplayName("update employee by id integration succeeds")
    void givenEmployee_whenUpdateEmployeeById_thenReturnUpdatedEmployee() throws Exception {

        Employee employee = Employee.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .email("new@gmail.com")
                .build();

        employeeRepository.save(employee);

        Employee updated = Employee.builder()
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .email("Updatednew@gmail.com")
                .build();

        ResultActions response = mockMvc.perform(
                put("/api/v1/employees/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated))
        );

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updated.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updated.getLastName())))
                .andExpect(jsonPath("$.email", is(updated.getEmail())));
    }

    @Test
    @DisplayName("update employee by id integration fails")
    void givenEmployee_whenUpdateEmployeeById_thenReturnUpdatedEmployeeFails() throws Exception {
        long wrongId = 1000000000000000000L;

        Employee updated = Employee.builder()
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .email("Updatednew@gmail.com")
                .build();

        ResultActions response = mockMvc.perform(
                put("/api/v1/employees/{id}", wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated))
        );

        response.andExpect(status().isNotFound()).andDo(
                print()
        );

    }

    @Test
    @DisplayName("delete employee by id integration succeeds")
    void givenEmployee_whenDeleteEmployeeById_thenEmployeeIsDeleted() throws Exception {

        Employee employee = Employee.builder()
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .email("Updatednew@gmail.com")
                .build();

        employeeRepository.save(employee);

        ResultActions response = mockMvc.perform(
                delete("/api/v1/employees/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk()).andDo(
                print()
        );
    }

    @Test
    @DisplayName("delete employee by id integration fails")
    void givenEmployee_whenDeleteEmployeeById_thenEmployeeIsNotDeleted() throws Exception {
        long wrongId = 1000000000000000000L;

        ResultActions response = mockMvc.perform(
                delete("/api/v1/employees/{id}", wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isNotFound()).andDo(
                print()
        );
    }

}
