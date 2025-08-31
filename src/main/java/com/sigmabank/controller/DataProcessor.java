package com.sigmabank.controller;
import com.sigmabank.model.*;
import com.sigmabank.parser.FileParser;
import com.sigmabank.util.CommandLineParser;
import java.util.*;
import java.util.stream.Collectors;

public class DataProcessor {
    private Map<String, Department> departments = new HashMap<>();
    private List<String> errorLines = new ArrayList<>();

    public void process(FileParser.ParsedData parsedData) {

        for (Manager manager : parsedData.getManagers().values()) {
            String deptName = manager.getDepartmentName();

            if (departments.containsKey(deptName)) {

                errorLines.add(manager.toString());
            } else {
                Department department = new Department(deptName, manager);
                departments.put(deptName, department);
            }
        }

        for (Employee employee : parsedData.getEmployees()) {
            boolean assigned = false;

            Manager manager = parsedData.getManagers().get(employee.getManagerId());

            if (manager != null) {
                String deptName = manager.getDepartmentName();
                Department department = departments.get(deptName);

                if (department != null) {
                    department.addEmployee(employee);
                    assigned = true;
                }
            }

            if (!assigned) {
                errorLines.add(employee.toString());
            }
        }

        errorLines.addAll(parsedData.getErrorLines());
    }

    public void sortDepartments(CommandLineParser.SortType sortType, CommandLineParser.SortOrder sortOrder) {
        if (sortType == CommandLineParser.SortType.NONE) {
            return;
        }

        for (Department department : departments.values()) {
            List<Employee> employees = department.getEmployees();

            Comparator<Employee> comparator = null;

            if (sortType == CommandLineParser.SortType.NAME) {
                comparator = Comparator.comparing(Employee::getName);
            } else if (sortType == CommandLineParser.SortType.SALARY) {

                comparator = (e1, e2) -> {
                    Double s1 = e1.getSalary();
                    Double s2 = e2.getSalary();

                    if (s1 == null && s2 == null) return 0;
                    if (s1 == null) return 1;
                    if (s2 == null) return -1;

                    return Double.compare(s1, s2);
                };
            }

            if (comparator != null) {
                if (sortOrder == CommandLineParser.SortOrder.DESC) {
                    comparator = comparator.reversed();
                }

                employees.sort(comparator);
            }
        }
    }

    public List<Statistics> calculateStatistics() {
        List<Statistics> statisticsList = new ArrayList<>();

        for (Department department : departments.values()) {
            List<Employee> validEmployees = department.getEmployees().stream()
                    .filter(Employee::hasValidSalary)
                    .collect(Collectors.toList());

            double min = 0.0;
            double max = 0.0;
            double mid = 0.0;

            if (!validEmployees.isEmpty()) {
                min = validEmployees.stream()
                        .mapToDouble(Employee::getSalary)
                        .min()
                        .orElse(0.0);

                max = validEmployees.stream()
                        .mapToDouble(Employee::getSalary)
                        .max()
                        .orElse(0.0);

                double sum = validEmployees.stream()
                        .mapToDouble(Employee::getSalary)
                        .sum();

                mid = sum / validEmployees.size();
            }

            min = Math.ceil(min * 100) / 100.0;
            max = Math.ceil(max * 100) / 100.0;
            mid = Math.ceil(mid * 100) / 100.0;

            Statistics stats = new Statistics(department.getName(), min, max, mid);
            statisticsList.add(stats);
        }

        statisticsList.sort(Comparator.comparing(Statistics::getDepartmentName));

        return statisticsList;
    }

    public Map<String, Department> getDepartments() { return departments; }
    public List<String> getErrorLines() { return errorLines; }
}
