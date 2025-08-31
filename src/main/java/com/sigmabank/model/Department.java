package com.sigmabank.model;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private Manager manager;
    private List<Employee> employees;

    public Department(String name, Manager manager) {
        this.name = name;
        this.manager = manager;
        this.employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public String getName() { return name; }
    public Manager getManager() { return manager; }
    public List<Employee> getEmployees() { return employees; }

}
