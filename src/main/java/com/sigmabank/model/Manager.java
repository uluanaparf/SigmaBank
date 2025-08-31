package com.sigmabank.model;

public class Manager {
    private int id;
    private String name;
    private Double salary;
    private String departmentName;

    public Manager(int id, String name, Double salary, String departmentName) {
        this.id = id;
        this.name = name != null ? name.trim() : name;
        this.salary = salary;
        this.departmentName = departmentName != null ? departmentName.trim() : departmentName;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public Double getSalary() { return salary; }

    public String getDepartmentName() { return departmentName; }

    @Override
    public String toString() {
        String salaryStr = salary != null ? String.valueOf(salary.intValue()) : "";
        return String.format("Manager,%d,%s,%s", id, name, salaryStr);
    }
}
