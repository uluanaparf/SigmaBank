package com.sigmabank.parser;

import com.sigmabank.model.Employee;
import com.sigmabank.model.Manager;

public class LineParser {
    public static class ParseResult {
        public enum Type { MANAGER, EMPLOYEE, ERROR }

        private Type type;
        private Manager manager;
        private Employee employee;
        private String errorLine;
        private String errorReason;

        private ParseResult(Type type) {
            this.type = type;
        }

        public static ParseResult manager(Manager manager) {
            ParseResult result = new ParseResult(Type.MANAGER);
            result.manager = manager;
            return result;
        }

        public static ParseResult employee(Employee employee) {
            ParseResult result = new ParseResult(Type.EMPLOYEE);
            result.employee = employee;
            return result;
        }

        public static ParseResult error(String line, String reason) {
            ParseResult result = new ParseResult(Type.ERROR);
            result.errorLine = line;
            result.errorReason = reason;
            return result;
        }

        public Type getType() { return type; }
        public Manager getManager() { return manager; }
        public Employee getEmployee() { return employee; }
        public String getErrorLine() { return errorLine; }

    }

    public ParseResult parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return ParseResult.error(line, "Пустая строка");
        }

        String trimmedLine = line.trim();
        String[] parts = trimmedLine.split(",", -1);

        if (parts.length != 5) {
            return ParseResult.error(trimmedLine, "Неверное количество полей");
        }

        String type = parts[0].trim();

        if ("Manager".equals(type)) {
            return parseManager(parts, trimmedLine);
        } else if ("Employee".equals(type)) {
            return parseEmployee(parts, trimmedLine);
        } else {
            return ParseResult.error(trimmedLine, "Неизвестный тип: " + type);
        }
    }

    private ParseResult parseManager(String[] parts, String originalLine) {
        try {
            int id = Integer.parseInt(parts[1].trim());
            String name = parts[2].trim();
            Double salary = parseSalary(parts[3].trim());
            String department = parts[4].trim();

            if (name.isEmpty()) {
                return ParseResult.error(originalLine, "Пустое имя менеджера");
            }

            if (department.isEmpty()) {
                return ParseResult.error(originalLine, "Пустое название департамента");
            }

            if (salary <= 0) {
                return ParseResult.error(originalLine, "Некорректная зарплата менеджера");
            }

            Manager manager = new Manager(id, name, salary, department);
            return ParseResult.manager(manager);

        } catch (NumberFormatException e) {
            return ParseResult.error(originalLine, "Некорректный ID менеджера");
        }
    }

    private ParseResult parseEmployee(String[] parts, String originalLine) {
        try {
            int id = Integer.parseInt(parts[1].trim());
            String name = parts[2].trim();
            Double salary = parseSalary(parts[3].trim());
            int managerId = Integer.parseInt(parts[4].trim());

            if (name.isEmpty()) {
                return ParseResult.error(originalLine, "Пустое имя сотрудника");
            }

            if (salary <= 0) {
                return ParseResult.error(originalLine, "Некорректная зарплата сотрудника");
            }

            Employee employee = new Employee(id, name, salary, managerId);
            return ParseResult.employee(employee);

        } catch (NumberFormatException e) {
            return ParseResult.error(originalLine, "Некорректный ID сотрудника или менеджера");
        }
    }

    private Double parseSalary(String salaryStr) throws NumberFormatException {
        if (salaryStr == null || salaryStr.trim().isEmpty()) {
            throw new NumberFormatException("Пустое поле зарплаты");
        }
        return Double.parseDouble(salaryStr.trim());
    }

}
