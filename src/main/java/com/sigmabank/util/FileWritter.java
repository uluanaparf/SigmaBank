package com.sigmabank.util;

import com.sigmabank.model.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Locale;

public class FileWritter {
    public void writeDepartmentFile(Department department) throws IOException {
        Path filePath = Paths.get("output", department.getName() + ".sb");
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {

            Manager manager = department.getManager();
            writer.write(formatManager(manager));
            writer.newLine();

            for (Employee employee : department.getEmployees()) {
                writer.write(formatEmployee(employee));
                writer.newLine();
            }
        }
    }

    public void writeAllDepartments(java.util.Map<String, Department> departments) throws IOException {
        for (Department department : departments.values()) {
            writeDepartmentFile(department);
        }
    }

    public void writeStatisticsToFile(List<Statistics> statistics, String path) throws IOException {

        Path filePath = Paths.get(path);
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("department,min,max,mid");
            writer.newLine();

            for (Statistics stat : statistics) {
                writer.write(formatStatistics(stat));
                writer.newLine();
            }
        }
    }

    public void printStatisticsToConsole(List<Statistics> statistics) {

        System.out.println("department,min,max,mid");

        for (Statistics stat : statistics) {
            System.out.println(formatStatistics(stat));
        }
    }

    private String formatManager(Manager manager) {
        String salaryStr = "";
        if (manager.getSalary() != null) {
            if (manager.getSalary() == manager.getSalary().intValue()) {
                salaryStr = String.valueOf(manager.getSalary().intValue());
            } else {
                salaryStr = String.valueOf(manager.getSalary());
            }
        }

        return String.format("Manager,%d,%s,%s",
                manager.getId(),
                manager.getName(),
                salaryStr);
    }

    private String formatEmployee(Employee employee) {
        String salaryStr = "";
        if (employee.getSalary() != null) {
            if (employee.getSalary() == employee.getSalary().intValue()) {
                salaryStr = String.valueOf(employee.getSalary().intValue());
            } else {
                salaryStr = String.valueOf(employee.getSalary());
            }
        }

        return String.format("Employee,%d,%s,%s,%d",
                employee.getId(),
                employee.getName(),
                salaryStr,
                employee.getManagerId());
    }

    private String formatStatistics(Statistics stat) {
        return String.format(Locale.US, "%s,%.2f,%.2f,%.2f",
                stat.getDepartmentName(),
                stat.getMin(),
                stat.getMax(),
                stat.getMid());
    }
}
