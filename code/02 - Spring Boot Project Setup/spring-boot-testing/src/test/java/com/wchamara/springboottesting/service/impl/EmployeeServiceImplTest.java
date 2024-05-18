package com.wchamara.springboottesting.service.impl;

import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * This class is used to test the EmployeeServiceImpl class.
 * It uses Mockito to mock the EmployeeRepository and inject it into the EmployeeServiceImpl.
 * It also uses JUnit 5 and AssertJ for testing.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    /**
     * A mock of the EmployeeRepository.
     */
    @Mock
    private EmployeeRepository employeeRepository;

    /**
     * The EmployeeServiceImpl to test.
     * The mock EmployeeRepository is injected into this instance.
     */
    @InjectMocks
    private EmployeeServiceImpl underTest;

    /**
     * An instance of Employee which will be used in the tests.
     */
    private Employee employee;

    /**
     * This method is called before each test.
     * It sets up the mock EmployeeRepository and the EmployeeServiceImpl to test.
     * It also initializes an Employee instance which will be used in the tests.
     */
    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Chamara")
                .lastName("Weerasinghe")
                .email("abc@gmail.com")
                .build();
    }

    /**
     * This method tests the saveEmployee method of the EmployeeServiceImpl.
     * It sets up a mock Employee and the expected behavior of the EmployeeRepository.
     * It then calls the saveEmployee method and verifies the result.
     */
    @Test
    @DisplayName("JUnit test for saveEmployee method")
    void given_when_thenSaveEmployee() {

        // given - precondition or setup
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());
        when(employeeRepository.save(employee)).thenReturn(employee);

        // when action or the behaviour we are going to test
        Employee savedEmployee = underTest.saveEmployee(employee);

        // then verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isEqualTo(employee.getId());
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(savedEmployee.getLastName()).isEqualTo(employee.getLastName());
        assertThat(savedEmployee.getEmail()).isEqualTo(employee.getEmail());

    }
}
