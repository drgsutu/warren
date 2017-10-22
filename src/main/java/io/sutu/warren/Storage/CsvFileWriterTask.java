package io.sutu.warren.Storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class CsvFileWriterTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CsvFileReaderTask.class);

    private final BlockingQueue<List<String>> OHLCVQueue;

    CsvFileWriterTask(BlockingQueue<List<String>> OHLCVQueue) {
        this.OHLCVQueue = OHLCVQueue;
    }

    @Override
    public void run() {
        String path = "ohlcv.csv";

        try (
                BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(path), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                PrintWriter writer = new PrintWriter(bufferedWriter)
        ) {
            try {
                while (!Thread.interrupted()) {
                    List<String> ohlcv = OHLCVQueue.take();
                    String csvLine = ohlcv.stream().collect(Collectors.joining(","));
                    writer.println(csvLine);
                }
            } catch (InterruptedException e) {
                logger.warn("Thread interrupted", e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
