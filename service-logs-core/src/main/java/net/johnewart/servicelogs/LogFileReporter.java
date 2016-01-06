package net.johnewart.servicelogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LogFileReporter implements Reporter {
    private final FileOutputStream logFile;
    private final Object lockObject = new Object();

    public LogFileReporter() throws FileNotFoundException {
        this("service.log");
    }

    public LogFileReporter(String logFilePath) throws FileNotFoundException {
        this.logFile = new FileOutputStream(new File(logFilePath));
    }

    public void report(RequestMetrics logEntry) {
        synchronized (lockObject) {
            try {
                logFile.write(logEntry.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
