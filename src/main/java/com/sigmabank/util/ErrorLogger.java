package com.sigmabank.util;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class ErrorLogger {
    private static final String ERROR_FILE = "output/error.log";

    public void writeErrors(List<String> errorLines) throws IOException {
        if (errorLines.isEmpty()) {
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ERROR_FILE))) {
            for (String errorLine : errorLines) {
                writer.write(errorLine);
                writer.newLine();
            }
        }
    }
}
