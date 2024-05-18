package com.wchamara.springboottesting.repository;

import com.wchamara.springboottesting.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class EmployeeRepositoryTest {
    // Autowired EmployeeRepository instance
    @Autowired
    private EmployeeRepository underTest;

    /**
     * This test case is for the Save Employee operation.
     * It uses JUnit's @DisplayName for better readability of test cases.
     * The test case follows the given-when-then pattern:
     * - given: An Employee object is created with some initial data.
     * - when: The save method of the EmployeeRepository is called with the created Employee object.
     * - then: Assertions are made to ensure that the saved Employee object is not null,
     * its id is greater than 0 (indicating successful save operation),
     * and the first name, last name, and email are as expected.
     */
    @DisplayName("JUnit5 test for Save Employee operation")
    @Test
    void givenEmployeeObject_whenSave_ThenReturnSavedEmployee() {
        // given
        Employee employee = Employee.builder()
                .firstName("Chamara")
                .lastName("Wijesekara")
                .email("abc@abc.com").build();
        // when
        Employee savedEmployee = underTest.save(employee);
        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(savedEmployee.getLastName()).isEqualTo(employee.getLastName());
        assertThat(savedEmployee.getEmail()).isEqualTo(employee.getEmail());
    }

    /**
     * This test case is for the Find All Employees operation.
     * It uses JUnit's @Test annotation to indicate that this is a test method.
     * The test case follows the given-when-then pattern:
     * - given: Two Employee objects are created with some initial data and saved using the save method of the EmployeeRepository.
     * - when: The findAll method of the EmployeeRepository is called.
     * - then: Assertions are made to ensure that the returned list of Employees is not null, its size is 2 (indicating both Employees were saved and retrieved successfully),
     * and the first name, last name, and email of each Employee in the list are as expected.
     */
    @DisplayName("JUnit5 test for get all Employees operation")
    @Test
    void given_when_thenName() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Chamara")
                .lastName("Wijesekara")
                .email("abc@abc.com").build();

        Employee employee2 = Employee.builder()
                .firstName("Gagani")
                .lastName("Dharika")
                .email("xds@abc.com").build();
        underTest.save(employee1);
        underTest.save(employee2);

        // when action or the behaviour we are going to test
        List<Employee> employees = underTest.findAll();
        // then verify the output
        assertThat(employees).hasSize(2);
        assertThat(employees).contains(employee1, employee2);

    }
}