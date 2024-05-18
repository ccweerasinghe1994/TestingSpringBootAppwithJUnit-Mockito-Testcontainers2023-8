package com.wchamara.springboottesting.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wchamara.springboottesting.model.Employee;

import java.util.List;

public class FileUtil {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Employee> readEmployees(String fileName) {
        try {
            return objectMapper.readValue(getClass().getClassLoader().getResourceAsStream(fileName), Employees.class).getUsers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
