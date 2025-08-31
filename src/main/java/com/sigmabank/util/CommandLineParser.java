package com.sigmabank.util;

import java.util.HashMap;
import java.util.Map;

public class CommandLineParser {
    public enum SortType {
        NONE, NAME, SALARY
    }

    public enum SortOrder {
        ASC, DESC
    }

    public enum OutputType {
        CONSOLE, FILE
    }

    private SortType sortType = SortType.NONE;
    private SortOrder sortOrder = null;
    private boolean generateStatistics = false;
    private OutputType outputType = OutputType.CONSOLE;
    private String outputPath = null;
    private boolean hasErrors = false;
    private String errorMessage = "";

    public void parse(String[] args) {
        Map<String, String> arguments = new HashMap<>();

        for (String arg : args) {
            if (arg.startsWith("--") || arg.startsWith("-")) {
                String[] parts = arg.split("=", 2);
                String key = parts[0];
                String value = parts.length > 1 ? parts[1] : "";

                if (key.equals("-s")) key = "--sort";
                if (key.equals("-o")) key = "--output";

                arguments.put(key, value);
            } else {
                hasErrors = true;
                errorMessage = "Неизвестный параметр: " + arg;
                return;
            }
        }

        processArguments(arguments);
    }

    private void processArguments(Map<String, String> arguments) {

        for (String key : arguments.keySet()) {
            if (!isValidParameter(key)) {
                hasErrors = true;
                errorMessage = "Неизвестный параметр: " + key;
                return;
            }
        }

        if (arguments.containsKey("--sort")) {
            String sortValue = arguments.get("--sort");
            if ("name".equals(sortValue)) {
                sortType = SortType.NAME;
            } else if ("salary".equals(sortValue)) {
                sortType = SortType.SALARY;
            } else {
                hasErrors = true;
                errorMessage = "Неправильный тип сортировки: " + sortValue;
                return;
            }
        }

        if (arguments.containsKey("--order")) {
            if (sortType == SortType.NONE) {
                hasErrors = true;
                errorMessage = "Параметр --order не может быть указан без --sort";
                return;
            }

            String orderValue = arguments.get("--order");
            if ("asc".equals(orderValue)) {
                sortOrder = SortOrder.ASC;
            } else if ("desc".equals(orderValue)) {
                sortOrder = SortOrder.DESC;
            } else {
                hasErrors = true;
                errorMessage = "Неправильный порядок сортировки: " + orderValue;
                return;
            }
        }

        if (sortType != SortType.NONE && sortOrder == null) {
            sortOrder = SortOrder.ASC;
        }

        if (arguments.containsKey("--stat")) {
            generateStatistics = true;
        }

        if (arguments.containsKey("--output")) {
            if (!generateStatistics) {
                hasErrors = true;
                errorMessage = "Параметр --output не может быть указан без --stat";
                return;
            }

            String outputValue = arguments.get("--output");
            if ("console".equals(outputValue)) {
                outputType = OutputType.CONSOLE;
            } else if ("file".equals(outputValue)) {
                outputType = OutputType.FILE;
            } else {
                hasErrors = true;
                errorMessage = "Неправильный тип вывода: " + outputValue;
                return;
            }
        }

        if (arguments.containsKey("--path")) {
            if (!generateStatistics) {
                hasErrors = true;
                errorMessage = "Параметр --path не может быть указан без --stat";
                return;
            }

            if (outputType != OutputType.FILE) {
                hasErrors = true;
                errorMessage = "Параметр --path может быть указан только с --output=file";
                return;
            }

            outputPath = arguments.get("--path");
            if (outputPath == null || outputPath.trim().isEmpty()) {
                hasErrors = true;
                errorMessage = "Путь к файлу не может быть пустым";
                return;
            }
        }

        if (outputType == OutputType.FILE && outputPath == null) {
            hasErrors = true;
            errorMessage = "Для --output=file необходимо указать --path";

        }
    }

    private boolean isValidParameter(String param) {
        return param.equals("--sort") || param.equals("-s") ||
                param.equals("--order") ||
                param.equals("--stat") ||
                param.equals("--output") || param.equals("-o") ||
                param.equals("--path");
    }

    public SortType getSortType() { return sortType; }
    public SortOrder getSortOrder() { return sortOrder; }
    public boolean isGenerateStatistics() { return generateStatistics; }
    public OutputType getOutputType() { return outputType; }
    public String getOutputPath() { return outputPath; }
    public boolean hasErrors() { return hasErrors; }
    public String getErrorMessage() { return errorMessage; }
}
