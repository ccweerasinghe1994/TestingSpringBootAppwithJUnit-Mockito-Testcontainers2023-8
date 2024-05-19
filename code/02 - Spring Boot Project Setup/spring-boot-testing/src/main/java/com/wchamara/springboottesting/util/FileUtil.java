package com.wchamara.springboottesting.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wchamara.springboottesting.model.Employee;

import java.util.List;

public class FileUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Employee> readEmployees(String fileName) {
        try {
            return objectMapper.readValue(FileUtil.class.getClassLoader().getResourceAsStream(fileName), Employees.class).getUsers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
