package com.sigmabank;

import com.sigmabank.model.Statistics;
import com.sigmabank.parser.FileParser;
import com.sigmabank.controller.DataProcessor;
import com.sigmabank.util.*;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            CommandLineParser cmdParser = new CommandLineParser();
            cmdParser.parse(args);

            if (cmdParser.hasErrors()) {
                System.err.println("Ошибка в параметрах: " + cmdParser.getErrorMessage());
                System.err.println("Использование:");
                System.err.println("  java -jar app.jar [--sort=name|salary] [--order=asc|desc] [--stat] [--output=console|file] [--path=<путь>]");
                System.exit(1);
            }

            FileParser fileParser = new FileParser();
            FileParser.ParsedData parsedData;

            try {
                parsedData = fileParser.parseAllSbFiles();
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файлов: " + e.getMessage());
                System.exit(1);
                return;
            }

            if (parsedData.getManagers().isEmpty()) {
                System.err.println("Не найдено ни одного менеджера в файлах .sb");
                ErrorLogger errorLogger = new ErrorLogger();
                try {
                    errorLogger.writeErrors(parsedData.getErrorLines());
                } catch (IOException e) {
                    System.err.println("Ошибка при записи error.log: " + e.getMessage());
                }
                System.exit(1);
                return;
            }

            java.nio.file.Path outputDir = java.nio.file.Paths.get("output");
            try {
                java.nio.file.Files.createDirectories(outputDir);
            } catch (IOException e) {
                System.err.println("Не удалось создать папку output: " + e.getMessage());
                System.exit(1);
            }

            DataProcessor processor = new DataProcessor();
            processor.process(parsedData);

            processor.sortDepartments(cmdParser.getSortType(), cmdParser.getSortOrder());

            FileWritter fileWritter = new FileWritter();
            try {
                fileWritter.writeAllDepartments(processor.getDepartments());
            } catch (IOException e) {
                System.err.println("Ошибка при записи файлов департаментов: " + e.getMessage());
                System.exit(1);
            }

            ErrorLogger errorLogger = new ErrorLogger();
            try {
                errorLogger.writeErrors(processor.getErrorLines());
            } catch (IOException e) {
                System.err.println("Ошибка при записи error.log: " + e.getMessage());
            }

            if (cmdParser.isGenerateStatistics()) {
                List<Statistics> statistics = processor.calculateStatistics();

                if (cmdParser.getOutputType() == CommandLineParser.OutputType.CONSOLE) {
                    fileWritter.printStatisticsToConsole(statistics);
                } else {
                    try {
                        fileWritter.writeStatisticsToFile(statistics, cmdParser.getOutputPath());
                    } catch (IOException e) {
                        System.err.println("Ошибка при записи файла статистики: " + e.getMessage());
                        System.exit(1);
                    }
                }
            }

            System.out.println("Обработка завершена успешно.");

        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}