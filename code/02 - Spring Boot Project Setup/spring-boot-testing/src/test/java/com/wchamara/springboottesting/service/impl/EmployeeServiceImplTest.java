package com.wchamara.springboottesting.service.impl;

import com.wchamara.springboottesting.exception.ResourceNotFoundException;
import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    void givenEmployeeObject_whenSaveEmployee_thenReturnEmployee() {

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

    /**
     * This method tests the saveEmployee method of the EmployeeServiceImpl when the email already exists.
     * It sets up a mock Employee and the expected behavior of the EmployeeRepository.
     * It then calls the saveEmployee method and expects a ResourceNotFoundException to be thrown.
     * <p>
     * The test follows the given-when-then pattern:
     * - Given: An Employee with an email that already exists is created and the findById method of the EmployeeRepository is set to return this Employee.
     * - When: The saveEmployee method is called with this Employee.
     * - Then: It is verified that a ResourceNotFoundException is thrown and that the save method of the EmployeeRepository is never called.
     */
    @Test
    @DisplayName("JUnit test for saveEmployee method which will throw exception when email already exists")
    void givenEmailAlreadyExists_whenSaveEmployee_thenWillThrowsException() {

        // given - precondition or setup
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        // when action or the behaviour we are going to test


        // then verify the output
//        assertThatThrownBy(() -> underTest.saveEmployee(employee))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessage("Employee already exists with given email : " + employee.getEmail());
        assertThrows(ResourceNotFoundException.class, () -> underTest.saveEmployee(employee));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    /**
     * This method tests the getAllEmployees method of the EmployeeServiceImpl.
     * It sets up a mock list of Employees and the expected behavior of the EmployeeRepository.
     * It then calls the getAllEmployees method and verifies the result.
     * <p>
     * The test follows the given-when-then pattern:
     * - Given: A list of Employees is created and the findAll method of the EmployeeRepository is set to return this list.
     * - When: The getAllEmployees method is called.
     * - Then: It is verified that the returned list is not null, has the correct size, and contains the correct Employees.
     */
    @Test
    @DisplayName("JUnit test for getAllEmployees method")
    void givenGetAllEmployees_whenGetAllEmployees_thenReturnListOfEmployees() {
        // given - precondition or setup
        List<Employee> employees = List.of(employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        // when action or the behaviour we are going to test
        List<Employee> allEmployees = underTest.getAllEmployees();
        // then verify the output
        assertThat(allEmployees).isNotNull();
        assertThat(allEmployees).hasSize(1);
        assertThat(allEmployees).contains(employee);

    }

    /**
     * This method tests the getAllEmployees method of the EmployeeServiceImpl when no Employees exist.
     * It sets up an empty list of Employees and the expected behavior of the EmployeeRepository.
     * It then calls the getAllEmployees method and expects an empty list to be returned.
     * <p>
     * The test follows the given-when-then pattern:
     * - Given: The findAll method of the EmployeeRepository is set to return an empty list.
     * - When: The getAllEmployees method is called.
     * - Then: It is verified that the returned list is empty.
     */
    @Test
    @DisplayName("JUnit test for getAllEmployees method when no employees exist")
    void givenNoEmployees_whenGetAllEmployees_thenReturnEmptyList() {
        // given - precondition or setup
        when(employeeRepository.findAll()).thenReturn(List.of());
        // when action or the behaviour we are going to test
        List<Employee> allEmployees = underTest.getAllEmployees();
        // then verify the output
        assertThat(allEmployees).isEmpty();
    }


    /**
     * This method tests the getEmployeeById method of the EmployeeServiceImpl.
     * It sets up a mock Employee and the expected behavior of the EmployeeRepository.
     * It then calls the getEmployeeById method and verifies the result.
     * <p>
     * The test follows the given-when-then pattern:
     * - Given: An Employee is created and the findById method of the EmployeeRepository is set to return this Employee.
     * - When: The getEmployeeById method is called with the id of this Employee.
     * - Then: It is verified that the returned Employee is not null and is the correct Employee.
     */
    @Test
    @DisplayName("JUnit test for getEmployeeById method")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() {
        // given - precondition or setup
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        // when action or the behaviour we are going to test
        Optional<Employee> employeeById = underTest.getEmployeeById(employee.getId());
        // then verify the output
        assertThat(employeeById).isPresent();
        assertThat(employeeById.get()).isEqualTo(employee);
    }

    /**
     * This method tests the getEmployeeById method of the EmployeeServiceImpl when the Employee does not exist.
     * It sets up a mock Employee and the expected behavior of the EmployeeRepository.
     * It then calls the getEmployeeById method and expects an empty Optional to be returned.
     * <p>
     * The test follows the given-when-then pattern:
     * - Given: The findById method of the EmployeeRepository is set to return an empty Optional.
     * - When: The getEmployeeById method is called with an id.
     * - Then: It is verified that the returned Optional is empty.
     */
    @Test
    @DisplayName("JUnit test for getEmployeeById method when Employee does not exist")
    void givenEmployeeId_whenGetEmployeeById_thenWillReturnEmptyOptional() {
        // given - precondition or setup
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());
        // when action or the behaviour we are going to test
        Optional<Employee> employeeById = underTest.getEmployeeById(employee.getId());
        // then verify the output
        assertThat(employeeById).isEmpty();
    }

    /**
     * This method tests the deleteEmployeeById method of the EmployeeServiceImpl.
     * It sets up the expected behavior of the EmployeeRepository.
     * It then calls the deleteEmployeeById method and verifies the result.
     * <p>
     * The test follows the given-when-then pattern:
     * - Given: An id is provided and the deleteById method of the EmployeeRepository is set to do nothing.
     * - When: The deleteEmployeeById method is called with this id.
     * - Then: It is verified that the deleteById method of the EmployeeRepository is called once with this id.
     */
    @Test
    @DisplayName("Employee deletion by id succeeds")
    void givenEmployeeId_whenDeleteEmployeeById_thenEmployeeIsDeleted() {
        // given - precondition or setup
        Long id = 1L;
        doNothing().when(employeeRepository).deleteById(id);

        // when action or the behaviour we are going to test
        underTest.deleteEmployeeById(id);

        // then verify the output
        verify(employeeRepository, times(1)).deleteById(id);
    }

    /**
     * This method tests the deleteEmployeeById method of the EmployeeServiceImpl when the Employee does not exist.
     * It sets up the expected behavior of the EmployeeRepository to throw an EmptyResultDataAccessException.
     * It then calls the deleteEmployeeById method and expects an EmptyResultDataAccessException to be thrown.
     * <p>
     * The test follows the given-when-then pattern:
     * - Given: An id is provided and the deleteById method of the EmployeeRepository is set to throw an EmptyResultDataAccessException.
     * - When: The deleteEmployeeById method is called with this id.
     * - Then: It is verified that an EmptyResultDataAccessException is thrown and that the deleteById method of the EmployeeRepository is called once with this id.
     */
    @Test
    @DisplayName("Employee deletion by non-existing id throws exception")
    void givenNonExistingEmployeeId_whenDeleteEmployeeById_thenThrowsException() {
        // given - precondition or setup
        Long id = 1L;
        doThrow(EmptyResultDataAccessException.class).when(employeeRepository).deleteById(id);

        // when action or the behaviour we are going to test

        // then verify the output
        assertThrows(EmptyResultDataAccessException.class, () -> underTest.deleteEmployeeById(id));
        verify(employeeRepository, times(1)).deleteById(id);
    }

    /**
     * This method tests the updateEmployee method of the EmployeeServiceImpl.
     * It sets up a mock Employee and the expected behavior of the EmployeeRepository.
     * It then calls the updateEmployee method and verifies the result.
     * <p>
     * The test follows the given-when-then pattern:
     * - Given: An id and an updated Employee are provided and the findById method of the EmployeeRepository is set to return the Employee.
     * - When: The updateEmployee method is called with this id and updated Employee.
     * - Then: It is verified that the returned Employee is not null and is the updated Employee.
     */
    @Test
    @DisplayName("Employee update by id succeeds")
    void givenEmployeeIdAndEmployee_whenUpdateEmployee_thenEmployeeIsUpdated() {
        // given - precondition or setup
        Long id = 1L;
        Employee updatedEmployee = Employee.builder()
                .id(id)
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .email("updated@gmail.com")
                .build();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // when action or the behaviour we are going to test
        Employee result = underTest.updateEmployee(id, updatedEmployee);

        // then verify the output
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(updatedEmployee.getId());
        assertThat(result.getFirstName()).isEqualTo(updatedEmployee.getFirstName());
        assertThat(result.getLastName()).isEqualTo(updatedEmployee.getLastName());
        assertThat(result.getEmail()).isEqualTo(updatedEmployee.getEmail());
    }

    /**
     * This method tests the updateEmployee method of the EmployeeServiceImpl when the Employee does not exist.
     * It sets up a mock Employee and the expected behavior of the EmployeeRepository.
     * It then calls the updateEmployee method and expects a ResourceNotFoundException to be thrown.
     * <p>
     * The test follows the given-when-then pattern:
     * - Given: An id and an updated Employee are provided and the findById method of the EmployeeRepository is set to return an empty Optional.
     * - When: The updateEmployee method is called with this id and updated Employee.
     * - Then: It is verified that a ResourceNotFoundException is thrown.
     */
    @Test
    @DisplayName("Employee update by non-existing id throws exception")
    void givenNonExistingEmployeeId_whenUpdateEmployee_thenThrowsException() {
        // given - precondition or setup
        Long id = 1L;
        Employee updatedEmployee = Employee.builder()
                .id(id)
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .email("updated@gmail.com")
                .build();
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // when action or the behaviour we are going to test

        // then verify the output
        assertThrows(ResourceNotFoundException.class, () -> underTest.updateEmployee(id, updatedEmployee));
    }

    @Test
    @DisplayName("Employee update by partial data succeeds")
    void givenEmployeeIdAndPartialEmployee_whenUpdateEmployee_thenEmployeeIsUpdated() {
        // given - precondition or setup
        Long id = 1L;
        Employee updatedEmployee = Employee.builder()
                .id(id)
                .firstName("UpdatedFirstName")
                .build();
        Employee existingEmployee = Employee.builder()
                .id(id)
                .firstName("Existing FirstName")
                .lastName("Existing LastName")
                .email("Existing@gmail.com")
                .build();
        Employee expectedEmployee = Employee.builder()
                .id(id)
                .firstName("UpdatedFirstName")
                .lastName("Existing LastName")
                .email("Existing@gmail.com")
                .build();

        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(expectedEmployee);

        // when action or the behaviour we are going to test
        Employee result = underTest.updateEmployee(id, updatedEmployee);

        // then verify the output
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(expectedEmployee.getId());
        assertThat(result.getFirstName()).isEqualTo(expectedEmployee.getFirstName());
        assertThat(result.getLastName()).isEqualTo(expectedEmployee.getLastName());
        assertThat(result.getEmail()).isEqualTo(expectedEmployee.getEmail());
    }
}
