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

/**
 * This class implements the EmployeeService interface.
 * It provides the business logic for managing Employees.
 */
public class EmployeeServiceImpl implements EmployeeService {

    /**
     * The repository for accessing the Employee data from the database.
     */
    private final EmployeeRepository employeeRepository;

    /**
     * Constructor for the EmployeeServiceImpl.
     * It takes an EmployeeRepository as a parameter and assigns it to the employeeRepository field.
     *
     * @param employeeRepository The repository for accessing the Employee data from the database.
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * This method is used to save an Employee to the database.
     * It first checks if an Employee with the same id already exists in the database.
     * If an Employee with the same id already exists, it throws a ResourceNotFoundException.
     * If no Employee with the same id exists, it saves the Employee to the database and returns the saved Employee.
     *
     * @param employee The Employee to save.
     * @return The saved Employee.
     * @throws ResourceNotFoundException If an Employee with the same id already exists.
     */
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

/**
 * This class extends the RuntimeException class.
 * It is used to indicate that a resource was not found.
 * It can be thrown when trying to find a resource with a specific id or other unique identifier, and the resource does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor for the ResourceNotFoundException.
     * It takes a message as a parameter and passes it to the superclass constructor.
     * The message should provide information about the resource that was not found.
     *
     * @param message The message about the resource that was not found.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor for the ResourceNotFoundException.
     * It takes a message and a cause as parameters and passes them to the superclass constructor.
     * The message should provide information about the resource that was not found.
     * The cause is the underlying exception that caused this exception to be thrown.
     *
     * @param message The message about the resource that was not found.
     * @param cause   The underlying exception that caused this exception to be thrown.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

```java
package com.wchamara.springboottesting.service.impl;

import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmployeeServiceImplTest {

    private EmployeeRepository employeeRepository;

    private EmployeeServiceImpl underTest;

    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        underTest = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    @DisplayName("JUnit test for saveEmployee method")
    void given_when_thenSaveEmployee() {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Chamara")
                .lastName("Weerasinghe")
                .email("abc@gmail.com")
                .build();
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
```

## 003 Quick Recap of Mockito basics (before writing JUnit tests to use Mock objects)

![alt text](image-3.png)
![alt text](image-4.png)
![alt text](image-5.png)
![alt text](image-6.png)

## 004 Unit test for EmployeeService saveEmployee method

```java
package com.wchamara.springboottesting.service.impl;

import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmployeeServiceImplTest {

    private EmployeeRepository employeeRepository;

    private EmployeeServiceImpl underTest;

    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        underTest = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    @DisplayName("JUnit test for saveEmployee method")
    void given_when_thenSaveEmployee() {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Chamara")
                .lastName("Weerasinghe")
                .email("abc@gmail.com")
                .build();
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
```

## 005 Using @Mock and @InjectMocks annotations to mock the object

```java
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

```

## 006 Unit test for saveEmployee method which throws Exception

```java
package com.wchamara.springboottesting.service;

import com.wchamara.springboottesting.model.Employee;

import java.util.List;

/**
 * This interface defines the service layer for managing Employees.
 * It declares a method for saving an Employee.
 * The implementation of this interface should provide the business logic for saving an Employee.
 */
public interface EmployeeService {

    /**
     * This method is used to save an Employee.
     * It takes an Employee as a parameter and returns the saved Employee.
     * The implementation of this method should handle the business logic for saving the Employee, such as checking if an Employee with the same id already exists.
     *
     * @param employee The Employee to save.
     * @return The saved Employee.
     */
    Employee saveEmployee(Employee employee);

    /**
     * This method is used to retrieve all Employees from the database.
     * The implementation of this method should handle the business logic for retrieving all Employees.
     *
     * @return A List of all Employees.
     */
    List<Employee> getAllEmployees();
}
```

```java
package com.wchamara.springboottesting.service.impl;

import com.wchamara.springboottesting.exception.ResourceNotFoundException;
import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import com.wchamara.springboottesting.service.EmployeeService;

import java.util.List;
import java.util.Optional;

/**
 * This class implements the EmployeeService interface.
 * It provides the business logic for managing Employees.
 */
public class EmployeeServiceImpl implements EmployeeService {

    /**
     * The repository for accessing the Employee data from the database.
     */
    private final EmployeeRepository employeeRepository;

    /**
     * Constructor for the EmployeeServiceImpl.
     * It takes an EmployeeRepository as a parameter and assigns it to the employeeRepository field.
     *
     * @param employeeRepository The repository for accessing the Employee data from the database.
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * This method is used to save an Employee to the database.
     * It first checks if an Employee with the same id already exists in the database.
     * If an Employee with the same id already exists, it throws a ResourceNotFoundException.
     * If no Employee with the same id exists, it saves the Employee to the database and returns the saved Employee.
     *
     * @param employee The Employee to save.
     * @return The saved Employee.
     * @throws ResourceNotFoundException If an Employee with the same id already exists.
     */
    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> findEmployee = employeeRepository.findById(employee.getId());

        if (findEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exists with given email : " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    /**
     * This method is used to retrieve all Employees from the database.
     * It calls the findAll method of the EmployeeRepository to retrieve all Employees.
     * It returns a List of all Employees.
     *
     * @return A List of all Employees.
     */
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
```

```java

```

## 007 Unit test for EmployeeService getAllEmployees method - Positive Scenario

```java
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
```

## 008 Unit test for EmployeeService getAllEmployees method - Negative Scenario

```java
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
```

```java
    /**
     * This method is used to retrieve all Employees from the database.
     * It calls the findAll method of the EmployeeRepository to retrieve all Employees.
     * It returns a List of all Employees.
     *
     * @return A List of all Employees.
     */
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
```

## 009 Unit test for  EmployeeService getEmployeeById method

```java
package com.wchamara.springboottesting.service;

import com.wchamara.springboottesting.model.Employee;

import java.util.List;
import java.util.Optional;

/**
 * This interface defines the service layer for managing Employees.
 * It declares a method for saving an Employee.
 * The implementation of this interface should provide the business logic for saving an Employee.
 */
public interface EmployeeService {

    /**
     * This method is used to save an Employee.
     * It takes an Employee as a parameter and returns the saved Employee.
     * The implementation of this method should handle the business logic for saving the Employee, such as checking if an Employee with the same id already exists.
     *
     * @param employee The Employee to save.
     * @return The saved Employee.
     */
    Employee saveEmployee(Employee employee);

    /**
     * This method is used to retrieve all Employees from the database.
     * The implementation of this method should handle the business logic for retrieving all Employees.
     *
     * @return A List of all Employees.
     */
    List<Employee> getAllEmployees();

    /**
     * This method is used to retrieve an Employee by id.
     * It takes the id of the Employee as a parameter and returns an Optional of the Employee.
     * The implementation of this method should handle the business logic for retrieving an Employee by id.
     *
     * @param id The id of the Employee to retrieve.
     * @return An Optional of the Employee.
     */
    Optional<Employee> getEmployeeById(Long id);

    /**
     * This method is used to delete an Employee by id.
     * It takes the id of the Employee as a parameter and returns void.
     * The implementation of this method should handle the business logic for deleting an Employee by id.
     *
     * @param id The id of the Employee to delete.
     */
    void deleteEmployeeById(Long id);

    /**
     * This method is used to update an Employee.
     * It takes the id of the Employee and the updated Employee as parameters and returns the updated Employee.
     * The implementation of this method should handle the business logic for updating an Employee.
     *
     * @param id       The id of the Employee to update.
     * @param employee The updated Employee.
     * @return The updated Employee.
     */
    Employee updateEmployee(Long id, Employee employee);
}
```

```java
package com.wchamara.springboottesting.service.impl;

import com.wchamara.springboottesting.exception.ResourceNotFoundException;
import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import com.wchamara.springboottesting.service.EmployeeService;

import java.util.List;
import java.util.Optional;

/**
 * This class implements the EmployeeService interface.
 * It provides the business logic for managing Employees.
 */
public class EmployeeServiceImpl implements EmployeeService {

    /**
     * The repository for accessing the Employee data from the database.
     */
    private final EmployeeRepository employeeRepository;

    /**
     * Constructor for the EmployeeServiceImpl.
     * It takes an EmployeeRepository as a parameter and assigns it to the employeeRepository field.
     *
     * @param employeeRepository The repository for accessing the Employee data from the database.
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * This method is used to save an Employee to the database.
     * It first checks if an Employee with the same id already exists in the database.
     * If an Employee with the same id already exists, it throws a ResourceNotFoundException.
     * If no Employee with the same id exists, it saves the Employee to the database and returns the saved Employee.
     *
     * @param employee The Employee to save.
     * @return The saved Employee.
     * @throws ResourceNotFoundException If an Employee with the same id already exists.
     */
    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> findEmployee = employeeRepository.findById(employee.getId());

        if (findEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exists with given email : " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    /**
     * This method is used to retrieve all Employees from the database.
     * It calls the findAll method of the EmployeeRepository to retrieve all Employees.
     * It returns a List of all Employees.
     *
     * @return A List of all Employees.
     */
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * This method is used to retrieve an Employee by their id from the database.
     * It calls the findById method of the EmployeeRepository with the provided id.
     * It returns an Optional that contains the Employee if one was found with the provided id, or an empty Optional if no Employee was found.
     *
     * @param id The id of the Employee to retrieve.
     * @return An Optional containing the Employee if one was found, or an empty Optional if no Employee was found.
     */
    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    /**
     * This method is used to delete an Employee by their id from the database.
     * It calls the deleteById method of the EmployeeRepository with the provided id.
     *
     * @param id The id of the Employee to delete.
     */
    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }

    /**
     * This method is used to update an Employee by their id in the database.
     * It first checks if an Employee with the provided id exists in the database.
     * If an Employee with the provided id exists, it updates the Employee with the provided data and returns the updated Employee.
     * If no Employee with the provided id exists, it throws a ResourceNotFoundException.
     *
     * @param id       The id of the Employee to update.
     * @param employee The Employee data to update.
     * @return The updated Employee.
     * @throws ResourceNotFoundException If no Employee with the provided id exists.
     */
    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Optional<Employee> findEmployee = employeeRepository.findById(id);

        if (findEmployee.isPresent()) {
            Employee existingEmployee = findEmployee.get();
            if (employee.getFirstName() != null) {
                existingEmployee.setFirstName(employee.getFirstName());
            }
            if (employee.getLastName() != null) {
                existingEmployee.setLastName(employee.getLastName());
            }

            if (employee.getEmail() != null) {
                existingEmployee.setEmail(employee.getEmail());
            }

            return employeeRepository.save(existingEmployee);
        } else {
            throw new ResourceNotFoundException("Employee not found with id : " + id);
        }
    }
}
```

```java
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

```

## 010 Unit test for EmployeeService updateEmployee method

```java
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
```

## 011 Unit test for EmployeeService deleteEmployee method

```java
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

```
