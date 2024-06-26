# 06 - Spring Boot - Integration Testing using Local MySQL Database

## 001 Integration testing overview

![alt text](image.png)
![alt text](image-1.png)
![alt text](image-2.png)

## 002 @SpringBootTest annotation overview

![alt text](image-3.png)
![alt text](image-4.png)
![alt text](image-5.png)

## 003 Configure MySQL database for integration testing

![alt text](image-6.png)

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
```

in your local machine, you need to install MySQL database and create a database called `ems` and a table called `employee` with the following schema.

or create a docker compose file to create a MySQL database.

```yaml
version: '3.7'

services:
  db:
    image: mysql:8.3.0
    command:
      - --default-authentication-plugin=mysql_native_password
    restart: always
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: example
```

```bash
mysql -u root -p
```

```sql
create database ems;
```

let's add the following properties to the `application.properties` file.

```properties
spring.application.name=spring-boot-testing
spring.jpa.show-sql=true
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/ems?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false
spring.datasource.username=root
spring.datasource.password=example
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

The provided code is a configuration file (`application.properties`) for a Spring Boot application. This file is used to configure various aspects of the application. Here's a breakdown of what each line does:

- `spring.application.name=spring-boot-testing`: This sets the name of the Spring Boot application to "spring-boot-testing".

- `spring.jpa.show-sql=true`: This enables the logging of all SQL statements that Hibernate generates.

- `spring.datasource.url=jdbc:mysql://127.0.0.1:3306/ems?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false`: This sets the JDBC URL for the MySQL database. The URL specifies that the database is running on localhost (`127.0.0.1`) on port `3306` and the database name is `ems`. The additional parameters configure the connection properties.

- `spring.datasource.username=root` and `spring.datasource.password=example`: These set the username and password to connect to the MySQL database.

- `spring.datasource.driver-class-name=com.mysql.cj.jdbc.`: This sets the JDBC driver class name for MySQL. However, it seems to be incomplete. It should be `com.mysql.cj.jdbc.Driver`.

- `spring.jpa.hibernate.ddl-auto=update`: This property is used to automatically create, update, or even drop database tables based on the entity classes. The `update` value means that Hibernate will update the schema whenever it sees that changes are needed.

## 004 Create a base for Integration testing

```java
package com.wchamara.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

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
}
```

## 005 Integration test for create employee REST API

```java
package com.wchamara.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

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


}

```

## 006 Integration test for get all employees REST API

```java
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
```

## 007 Integration test for get employee by id REST API - Positive & Negative Scenarios

```java
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
```

## 008 Integration test for update employee REST API - Positive & Negative Scenarios

```java
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

```

## 009 Integration test for delete employee REST API

```java
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
```

## 010 Integration testing EmployeeRepository using MySQL database

```java
package com.wchamara.springboottesting.integration;

import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is a test class for EmployeeRepository.
 * It uses Spring Boot's @DataJpaTest for configuration.
 *
 * @DataJpaTest provides some standard setup needed for testing the persistence layer:
 * - configuring H2, an in-memory database
 * - setting Hibernate, Spring Data, and the DataSource
 * - performing an @EntityScan
 * - turning on SQL logging
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryITest {
    // Autowired EmployeeRepository instance
    @Autowired
    private EmployeeRepository underTest;

    private Employee employee;

}
```

The selected code is a set of integration tests for the `EmployeeRepository` class in a Spring Boot application. The tests are written in Java using JUnit 5 and Spring Boot's `@DataJpaTest` for testing the persistence layer.

The `EmployeeRepositoryITest` class is annotated with `@DataJpaTest` and `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)`. The `@DataJpaTest` annotation provides some standard setup needed for testing the persistence layer, such as configuring H2, an in-memory database, setting Hibernate, Spring Data, and the DataSource, performing an `@EntityScan`, and turning on SQL logging. The `@AutoConfigureTestDatabase` annotation is used to replace the existing DataSource with an auto-configured one.

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryITest {
    @Autowired
    private EmployeeRepository underTest;
    ...
}
```
