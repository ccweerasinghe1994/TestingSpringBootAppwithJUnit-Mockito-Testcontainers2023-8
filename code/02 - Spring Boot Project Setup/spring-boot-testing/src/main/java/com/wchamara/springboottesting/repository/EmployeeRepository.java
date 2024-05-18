package com.wchamara.springboottesting.repository;

import com.wchamara.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    /**
     * This method is used to find an Employee by their email.
     * It takes an email as a parameter and returns an Optional<Employee>.
     * The Optional<Employee> will be empty if no Employee is found with the given email.
     *
     * @param email The email of the Employee to find.
     * @return An Optional<Employee> containing the Employee if found, or empty if not found.
     */
    Optional<Employee> findByEmail(String email);

    /**
     * This method is used to find an Employee by their first name and last name using a JPQL query.
     * It takes a first name and a last name as parameters and returns an Employee.
     * If no Employee is found with the given first name and last name, it will return null.
     *
     * @param firstName The first name of the Employee to find.
     * @param lastName  The last name of the Employee to find.
     * @return The Employee if found, or null if not found.
     */
    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
    Employee findByJPQLQuery(String firstName, String lastName);

    /**
     * This method is used to find an Employee by their first name and last name using a JPQL query with named parameters.
     * It takes a first name and a last name as parameters and returns an Employee.
     * If no Employee is found with the given first name and last name, it will return null.
     * The named parameters in the JPQL query provide a more readable and error-prone way of setting parameters.
     *
     * @param firstName The first name of the Employee to find.
     * @param lastName  The last name of the Employee to find.
     * @return The Employee if found, or null if not found.
     */
    @Query("SELECT e FROM Employee e WHERE e.firstName =:firstName AND e.lastName =:lastName")
    Employee findByJPQLQueryWithNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);

    /**
     * This method is used to find an Employee by their first name and last name using a native SQL query.
     * It takes a first name and a last name as parameters and returns an Employee.
     * If no Employee is found with the given first name and last name, it will return null.
     * The native SQL query provides a way to write database-specific queries, which can be more efficient in some cases.
     * The parameters in the query are indexed, starting from 1.
     *
     * @param firstName The first name of the Employee to find.
     * @param lastName  The last name of the Employee to find.
     * @return The Employee if found, or null if not found.
     */
    @Query(value = "SELECT * FROM employees e WHERE e.first_name = ?1 AND e.last_name = ?2", nativeQuery = true)
    Employee findByNativeQueryWithIndexParameters(String firstName, String lastName);

    /**
     * This method is used to find an Employee by their first name and last name using a native SQL query with named parameters.
     * It takes a first name and a last name as parameters and returns an Employee.
     * If no Employee is found with the given first name and last name, it will return null.
     * The native SQL query provides a way to write database-specific queries, which can be more efficient in some cases.
     * The named parameters in the query provide a more readable and error-prone way of setting parameters.
     *
     * @param firstName The first name of the Employee to find.
     * @param lastName  The last name of the Employee to find.
     * @return The Employee if found, or null if not found.
     */
    @Query(value = "SELECT * FROM employees e WHERE e.first_name =:firstName AND e.last_name =:lastName", nativeQuery = true)
    Employee findByNativeQueryWithNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
