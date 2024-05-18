package com.wchamara.springboottesting.util;

import com.wchamara.springboottesting.model.Employee;
import lombok.Data;

import java.util.List;

@Data
public class Employees {
    private List<Employee> users;
}
