package com.wchamara.springboottesting.util;

import com.wchamara.springboottesting.model.Employee;
import com.wchamara.springboottesting.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitialSetup implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    public InitialSetup(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        String fileName = "sample-data/user.json";

        List<Employee> users = FileUtil.readEmployees(fileName);

        System.out.println("running...");

        employeeRepository.saveAll(users);

    }
}
