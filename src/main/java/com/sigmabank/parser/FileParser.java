package com.sigmabank.parser;
import com.sigmabank.model.Employee;
import com.sigmabank.model.Manager;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileParser {
    public static class ParsedData {
        private Map<Integer, Manager> managers = new HashMap<>();
        private List<Employee> employees = new ArrayList<>();
        private List<String> errorLines = new ArrayList<>();

        public void addManager(Manager manager) {
            managers.put(manager.getId(), manager);
        }

        public void addEmployee(Employee employee) {
            employees.add(employee);
        }

        public void addError(String errorLine) {
            errorLines.add(errorLine);
        }

        public Map<Integer, Manager> getManagers() { return managers; }
        public List<Employee> getEmployees() { return employees; }
        public List<String> getErrorLines() { return errorLines; }
    }

    private LineParser lineParser = new LineParser();

    public ParsedData parseAllSbFiles() throws IOException {
        ParsedData result = new ParsedData();

        Path currentDir = Paths.get(".");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir, "*.sb")) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    parseFile(file, result);
                }
            }
        }

        return result;
    }

    private void parseFile(Path file, ParsedData result) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                LineParser.ParseResult parseResult = lineParser.parseLine(line);

                switch (parseResult.getType()) {
                    case MANAGER:
                        Manager manager = parseResult.getManager();

                        if (result.getManagers().containsKey(manager.getId())) {
                            result.addError(line);
                        } else {
                            result.addManager(manager);
                        }
                        break;

                    case EMPLOYEE:
                        result.addEmployee(parseResult.getEmployee());
                        break;

                    case ERROR:
                        result.addError(parseResult.getErrorLine());
                        break;
                }
            }
        }
    }
}
