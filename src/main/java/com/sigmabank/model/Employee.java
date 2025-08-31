package com.sigmabank.model;

public class Employee {
    private int id;
    private String name;
    private Double salary;
    private int managerId;

    public Employee(int id, String name, Double salary, int managerId) {
        this.id = id;
        this.name = name != null ? name.trim() : name;
        this.salary = salary;
        this.managerId = managerId;
    }

    public boolean hasValidSalary() {
        return salary != null && salary > 0;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public Double getSalary() { return salary; }

    public int getManagerId() { return managerId; }

    @Override
    public String toString() {
        String salaryStr = salary != null ? String.valueOf(salary.intValue()) : "";
        return String.format("Employee,%d,%s,%s,%d", id, name, salaryStr, managerId);
    }
}
